const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');

process.on('uncaughtException', (err) => {
  console.error('[UNCAUGHT EXCEPTION]', err);
});
process.on('unhandledRejection', (reason, p) => {
  console.error('[UNHANDLED REJECTION]', reason);
});

const PORT = 3000;
const ROOT_DIR = path.resolve(__dirname);
const ASSETS_DIR = path.resolve(__dirname, 'app/src/main/assets');
const PUBLIC_DIR = path.resolve(__dirname, 'public');

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.mjs': 'application/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.gif': 'image/gif',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon',
  '.webp': 'image/webp',
  '.txt': 'text/plain; charset=utf-8'
};

function serveFile(filePath, req, res, contentType) {
  try {
    const stat = fs.statSync(filePath);
    res.writeHead(200, {
      'Content-Type': contentType,
      'Content-Length': stat.size
    });

    if (req.method === 'HEAD') {
      res.end();
      return;
    }

    const stream = fs.createReadStream(filePath);
    stream.on('error', (err) => {
      console.error('[STREAM ERROR]', err);
      if (!res.headersSent) res.writeHead(500);
      res.end();
    });
    res.on('error', (err) => {
      console.error('[RES ERROR]', err);
      stream.destroy();
    });
    stream.pipe(res);
  } catch (err) {
    console.error('[SERVE ERROR]', err);
    if (!res.headersSent) res.writeHead(500);
    res.end();
  }
}

const server = http.createServer((req, res) => {
  const timestamp = new Date().toISOString();
  console.log(`[REQ ${timestamp}] ${req.method} ${req.url} | Host: ${req.headers.host} | UA: ${req.headers['user-agent'] ? req.headers['user-agent'].slice(0, 50) : 'none'}`);

  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, HEAD, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', '*');
  res.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate');

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  const parsedUrl = url.parse(req.url);
  let pathname = decodeURIComponent(parsedUrl.pathname || '/');

  if (pathname.endsWith('/') && pathname.length > 1) {
    pathname = pathname.slice(0, -1);
  }

  // 1. Root or index.html requests
  if (pathname === '/' || pathname === '/index.html') {
    const indexPath = path.join(ASSETS_DIR, 'index.html');
    if (fs.existsSync(indexPath)) {
      serveFile(indexPath, req, res, 'text/html; charset=utf-8');
      return;
    }
  }

  // 2. Candidate asset search paths
  const candidates = [
    path.join(ASSETS_DIR, pathname),
    path.join(PUBLIC_DIR, pathname),
    path.join(ROOT_DIR, pathname)
  ];

  for (const candidate of candidates) {
    if (fs.existsSync(candidate)) {
      const stat = fs.statSync(candidate);
      if (stat.isFile()) {
        const ext = path.extname(candidate).toLowerCase();
        const contentType = MIME_TYPES[ext] || 'application/octet-stream';
        serveFile(candidate, req, res, contentType);
        return;
      } else if (stat.isDirectory()) {
        const dirIndex = path.join(candidate, 'index.html');
        if (fs.existsSync(dirIndex)) {
          serveFile(dirIndex, req, res, 'text/html; charset=utf-8');
          return;
        }
      }
    }
  }

  // 3. Fallback to index.html for SPA/subroutes
  const fallbackIndex = path.join(ASSETS_DIR, 'index.html');
  if (fs.existsSync(fallbackIndex)) {
    serveFile(fallbackIndex, req, res, 'text/html; charset=utf-8');
    return;
  }

  res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
  res.end('Not Found');
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`[MS ZenoMart Preview Server] Listening on http://0.0.0.0:${PORT}`);
});
