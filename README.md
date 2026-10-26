# Laboratorio 1 — Spring Boot + React (Cool Cars)

## Estructura
- `server/` — API REST con Spring Boot 4, JPA, H2, Spring Data REST y Lombok (Java 25+).
- `client/` — Frontend React 19 + TypeScript con Vite.

## Ejecutar
1. Backend (puerto 8080):
   ```
   cd server
   ./mvnw spring-boot:run      (en Windows: mvnw.cmd spring-boot:run)
   ```
2. Frontend (puerto 3000):
   ```
   cd client
   npm install
   npm run dev
   ```
3. Abrir https://localhost:3000 (o http:// si no hay certificados, ver SSL)

Para ver los GIFs de GIPHY copia `client/.env.example` como `client/.env.local` y coloca tu API key.
Sin key se muestra una imagen por defecto.

## PWA
El manifiesto ("Good Car", ícono `car.png` de 512px) y el service worker los genera `vite-plugin-pwa`
(configurado en `client/vite.config.ts`). El service worker solo existe en la versión compilada:
```
cd client
npm run build
npm run serve       (como en la guía: serve -s dist en http://localhost:3000)
npm run preview     (la misma build, pero con HTTPS)
```
Si después vuelves a `npm run dev` y la página se ve desactualizada, quita el service worker en
Chrome: F12 → Application → Service workers → Unregister.

## SSL
`client/.env` activa HTTPS con los certificados `cert.pem` y `key.pem`, generados con:
```
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes -subj "/CN=localhost"
```
Los `.pem` no se suben a git. Si no existen, Vite arranca en HTTP normal.
Al ser un certificado autofirmado, Chrome mostrará una advertencia: "Configuración avanzada" → "Continuar a localhost".

## Lighthouse
Con la build corriendo (`npm run preview`), abre la app en Chrome → F12 → pestaña **Lighthouse** → **Analyze page load**.

## Endpoints
| Método | URL | Descripción |
|---|---|---|
| GET | `/cool-cars` | Carros "lujosos" (filtra AMC Gremlin, Triumph Stag, Ford Pinto, Yugo GV) |
| GET | `/cars` | Todos los carros |
| GET | `/cars/{id}` | Un carro |
| POST | `/cars` | Crear carro (valida `name` obligatorio, año 1886–2100, precio ≥ 0) |
| PUT | `/cars/{id}` | Actualizar carro (404 si no existe) |
| — | `/` o `/api` | HAL Explorer (Spring Data REST); la raíz redirige a `/api` |
| — | `/h2-console` | Consola H2 (JDBC URL `jdbc:h2:mem:testdb`, usuario `sa`, sin contraseña) |

Atributos de `Car`: `id`, `name`, `model`, `color`, `year`, `price`.

## Pruebas
```
cd server
./mvnw test
```
