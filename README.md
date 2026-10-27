# MediSoft | Módulo web de citas (GA7-220501096-AA2-EV02)

Aplicación Java web para gestionar citas médicas de MediSoft usando Servlets, formularios HTML y JSP. Continúa el módulo CRUD de citas de la evidencia EV01 y adapta su flujo de registro, consulta, actualización y cancelación al prototipo front-end elaborado previamente.

## Criterios implementados

1. **Servlets y JSP:** `CitaServlet` funciona como controlador MVC; JSP genera las vistas y recibe los atributos preparados por el Servlet.
2. **Formularios HTML:** el formulario de cita crea y actualiza registros. El formulario de búsqueda consulta mediante GET.
3. **GET y POST:** GET lista, busca y prepara formularios; POST procesa el registro, actualización y eliminación.
4. **Elementos JSP:** directivas `page` y `taglib`, EL (`${...}`), JSTL (`c:if`, `c:forEach`, `c:choose`, `c:out`) y formularios HTML.
5. **Base de datos:** el Servlet utiliza el DAO JDBC de EV01 para ejecutar el CRUD sobre MySQL con `PreparedStatement`.
6. **Versionamiento y estándar:** Git; paquetes en minúscula, clases `UpperCamelCase`, métodos y variables `lowerCamelCase`, constantes `UPPER_SNAKE_CASE`, separación MVC y cierre de recursos con try-with-resources.

## Estructura
```text
src/main/java/co/medisoft/citas/
├── web/CitaServlet.java                  # Controlador y métodos doGet/doPost
├── dao/CitaDao.java                      # Operaciones JDBC
├── database/ConexionBD.java              # Conexión MySQL
└── model/Cita.java                       # Entidad del dominio
src/main/webapp/
├── WEB-INF/views/                        # Vistas JSP no accesibles directamente
│   ├── lista-citas.jsp
│   └── formulario-cita.jsp
└── assets/styles.css
database/schema.sql
```

## Requisitos

- JDK 17.
- Maven 3.8 o superior.
- MySQL 8.0 o superior.
- Apache Tomcat 9.0 (Servlet 4.0 y JSP 2.3).

Servlet API se declara con alcance `provided`, porque Tomcat ya incluye esa API. El proyecto usa el paquete `javax.servlet` para corresponder con Tomcat 9 y con los ejemplos del componente formativo.

## Preparar MySQL

1. Inicia el servicio MySQL.
2. Ejecuta `database/schema.sql` desde MySQL Workbench para crear la base independiente `medisoft_ev02` y su tabla `citas`. EV02 no lee ni modifica la base `medisoft` de EV01.
3. Crea un usuario de aplicación con permisos CRUD:

```sql
CREATE USER IF NOT EXISTS 'medisoft_ev02_app'@'localhost' IDENTIFIED BY 'CAMBIA_ESTA_CLAVE';
ALTER USER 'medisoft_ev02_app'@'localhost' IDENTIFIED BY 'CAMBIA_ESTA_CLAVE';
GRANT SELECT, INSERT, UPDATE, DELETE ON medisoft_ev02.citas TO 'medisoft_ev02_app'@'localhost';
FLUSH PRIVILEGES;
```

Reemplaza la contraseña de ejemplo por una contraseña local. Esta es una cuenta distinta a la de EV01. No agregues la contraseña a Git.

## Configurar conexión local

En la misma ventana PowerShell desde la que arrancarás Tomcat, configura:

```powershell
$env:MEDISOFT_DB_URL = 'jdbc:mysql://localhost:3306/medisoft_ev02?serverTimezone=UTC'
$env:MEDISOFT_DB_USER = 'medisoft_ev02_app'
$secureDbPassword = Read-Host 'Contraseña de medisoft_ev02_app' -AsSecureString
$env:MEDISOFT_DB_PASSWORD = (New-Object System.Net.NetworkCredential('', $secureDbPassword)).Password
```

Las variables del ejemplo duran mientras esa ventana siga abierta.

## Empaquetar y desplegar en Tomcat

1. Instala Maven y descarga la distribución binaria ZIP de Tomcat 9 desde sus sitios oficiales.
2. Extrae Tomcat, por ejemplo, en `C:\tools\apache-tomcat-9.0.x`.
3. Desde la carpeta del proyecto, genera el WAR:

```powershell
mvn clean package
```

4. Copia el WAR a la carpeta `webapps` de Tomcat:

```powershell
$tomcat = 'C:\tools\apache-tomcat-9.0.x'
Copy-Item '.\target\medisoft-citas.war' (Join-Path $tomcat 'webapps')
```

5. En esa misma ventana PowerShell, arranca Tomcat:

```powershell
& (Join-Path $tomcat 'bin\catalina.bat') run
```

6. Abre `http://localhost:8080/medisoft-citas/citas` en el navegador. Para apagar Tomcat, vuelve a la consola y presiona `Ctrl+C`.

La primera compilación necesita acceso a Maven Central para descargar dependencias. Tomcat 9 implementa Servlet 4.0 y JSP 2.3, las APIs usadas por esta aplicación.

## Flujo de peticiones

| Acción | Solicitud | Responsable |
|---|---|---|
| Ver citas | `GET /citas` | `doGet` consulta el DAO de `medisoft_ev02` y reenvía a `lista-citas.jsp` |
| Buscar por ID | `GET /citas?accion=buscar&id=...` | `doGet` lee el parámetro GET y consulta el DAO |
| Abrir formulario | `GET /citas?accion=nuevo` | `doGet` prepara y muestra `formulario-cita.jsp` |
| Abrir edición | `GET /citas?accion=editar&id=...` | `doGet` recupera la cita y muestra el formulario |
| Registrar | `POST /citas` con `accion=crear` | `doPost` valida campos e inserta con JDBC |
| Actualizar | `POST /citas` con `accion=actualizar` | `doPost` valida campos y actualiza con JDBC |
| Eliminar | `POST /citas` con `accion=eliminar` | `doPost` elimina con JDBC |

## Versionamiento

El directorio incluye un repositorio Git local con commit inicial. Crea un repositorio remoto vacío y agrega su URL:

```powershell
git remote add origin URL_DEL_REPOSITORIO
git push -u origin main
```

Escribe la URL remota final en `repositorio.txt` antes de entregar la carpeta comprimida.

## Pruebas manuales

Consulta `docs/protocolo-pruebas.md` para los escenarios de registro, consulta GET, actualización, eliminación, validación y comprobación de Git.


