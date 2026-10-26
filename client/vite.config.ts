import { existsSync, readFileSync } from 'node:fs'
import react from '@vitejs/plugin-react'
import { defineConfig, loadEnv } from 'vite'
import { VitePWA } from 'vite-plugin-pwa'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')

  // HTTPS=true + certificados de OpenSSL (ver README), igual que en la guía con Create React App
  const useHttps = env.HTTPS === 'true' && existsSync(env.SSL_CRT_FILE ?? '') && existsSync(env.SSL_KEY_FILE ?? '')
  const https = useHttps
    ? { cert: readFileSync(env.SSL_CRT_FILE), key: readFileSync(env.SSL_KEY_FILE) }
    : undefined

  return {
    plugins: [
      react(),
      VitePWA({
        registerType: 'autoUpdate',
        includeAssets: ['favicon.png'],
        manifest: {
          short_name: 'Car',
          name: 'Good Car',
          description: 'Lista de carros con Spring Boot y React',
          lang: 'es',
          icons: [
            { src: 'favicon.png', sizes: '64x64', type: 'image/png' },
            { src: 'car-192.png', sizes: '192x192', type: 'image/png' },
            { src: 'car.png', sizes: '512x512', type: 'image/png' },
            { src: 'car.png', sizes: '512x512', type: 'image/png', purpose: 'maskable' },
          ],
          start_url: './index.html',
          display: 'standalone',
          theme_color: '#20232a',
          background_color: '#ffffff',
        },
      }),
    ],
    // Puerto 3000 para que coincida con el origen permitido por CORS en el backend
    server: { port: 3000, strictPort: true, https },
    preview: { port: 3000, strictPort: true, https },
  }
})
