import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// La Home React viene servita DENTRO l'app Spring Boot (non come server Node separato).
// Vite compila il bundle in src/main/resources/static/app/ con nomi FISSI (home.js / home.css)
// così la index.html Thymeleaf può referenziarli con un path stabile.
export default defineConfig({
  plugins: [react()],
  // Tutti gli asset sono serviti da Spring sotto /app/
  base: '/app/',
  build: {
    outDir: '../src/main/resources/static/app',
    emptyOutDir: true,
    manifest: false,
    rollupOptions: {
      // Entry = file JS (non una pagina HTML): il bundle viene incluso nella index.html Thymeleaf.
      input: 'src/main.jsx',
      output: {
        entryFileNames: 'home.js',
        chunkFileNames: 'home-[name].js',
        assetFileNames: 'home.[ext]',
        manualChunks: undefined, // bundle singolo: niente split vendor → un solo home.js
      },
    },
  },
})
