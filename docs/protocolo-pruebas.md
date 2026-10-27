# Protocolo de pruebas manuales — MediSoft EV02

Completa el campo de resultado durante la ejecución local. Usa datos ficticios. Requisito para ejecutar: base independiente `medisoft_ev02` configurada, variables `MEDISOFT_DB_*` activas con el usuario `medisoft_ev02_app`, WAR desplegado en Tomcat y navegador en `http://localhost:8080/medisoft-citas/citas`.

| ID | Escenario | Pasos | Resultado esperado | Resultado / fecha |
|---|---|---|---|---|
| P01 | Carga de listado | Abrir `/citas`. | La vista JSP carga el listado o el mensaje de estado vacío. | Pendiente |
| P02 | Consulta GET | Ingresar un ID conocido en el formulario y pulsar Buscar. | La URL contiene `accion=buscar&id=...` y se muestra el registro. | Pendiente |
| P03 | Registro POST | Abrir Registrar cita, diligenciar campos válidos y enviar. | El Servlet recibe POST, inserta por JDBC y muestra el registro en la lista. | Pendiente |
| P04 | Validación del formulario | Intentar enviar el formulario sin un campo requerido. | El navegador impide el envío; si se envía una petición manual, el Servlet retorna al formulario con mensaje. | Pendiente |
| P05 | Edición GET + actualización POST | Abrir Editar para un ID, cambiar un valor y guardar. | GET presenta el registro; POST persiste el cambio y la lista refleja el nuevo dato. | Pendiente |
| P06 | Eliminación POST | Pulsar Eliminar y confirmar en el diálogo. | Se envía POST y el registro deja de aparecer en la lista. | Pendiente |
| P07 | ID inexistente | Buscar un ID que no exista. | Se muestra un mensaje informativo y no se produce error del servidor. | Pendiente |
| P08 | Estado inválido | Enviar un estado distinto de PROGRAMADA, ATENDIDA o CANCELADA. | El Servlet rechaza el valor y presenta un mensaje de validación. | Pendiente |
| P09 | Versionamiento | Ejecutar `git status --short --branch` y `git log --oneline`. | Se identifica la rama y los commits del proyecto sin archivos secretos. | Pendiente |


## Registro de ejecución

- Equipo / sistema operativo: __________________________________
- Versión de Java (`java -version`): ____________________________
- Versión de Maven (`mvn -version`): ____________________________
- Versión de MySQL: ____________________________________________
- Versión de Tomcat: ___________________________________________
- Ejecutado por: ______________________________________________
- Fecha: ______________________________________________________
- Observaciones / errores: _____________________________________

