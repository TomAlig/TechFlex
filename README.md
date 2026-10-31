# TechFlex
Proyecto Semestral Desarrollo de Aplicaciones Móviles

## ▪ **¿En qué consiste?**
Esta aplicación móvil tiene como objetivo funcionar como intermediario entre productores y prestadores de la empresa "TECMUNDO" para volver las transacciones, la gestión de solicitudes y la oferta entre ellos más simple y dinámica.

Ofrece un apartado gráfico minimalista e intuitivo, con espacios específicos para los diferentes perfiles.

## ▪ **Desarrollo de la aplicación**
La aplicación cuenta con apartados compuestos por componentes modularizados que permiten el efectivo desarrollo asincrónico del equipo.
- Globales: Apartados que ambos perfiles de usuario (o ninguno) utilizan. como la vista de inicio de sesión, registro, o gestión de perfil.
- Productores: Vistas como las de gestión de solicitudes, la creación de una nueva solicitud y las ofertas disponibles respecto a lo que ofrecen los prestadores.
- Prestadores: Vistas como las de solicitudes cerca del prestador correspondiente, y el detalle de la oferta que va a ser mostrada al productor solicitante.

<br> A la hora de desarrollar este proyecto, utilizamos Pull Requests desde Features/Caracteristicas, fusionando a main tras una reunión en la cual definimos su integración como correcta.

## ▪ Flujo de usuario esperado según perfil registrado
Al ingresar con su usuario... <br>

### Productor

1.- Necesita un servicio (fumigación, drones), para el cual crea una nueva solicitud, llenando los detalles necesarios para la entrega. <br>
2.- Verifica el estado de su solicitud mediante "mis solicitudes" o notificaciones que se envían directo a su correo. En futuras versiones, podrá también editar la solicitud, o eliminarla antes de ser atendida por un prestador. <br>
3.- En la vista ofertas, puede revisar distintas opciones de prestadores que cumplen con su necesidad o con alternativas, para que pueda elegir contactarlo y tener mas información al respecto, o aceptar la solicitud y pasar a una tramitación.

### Prestador

1.- Puede revisar las solicitudes subidas por los productores a través de la vista de solicitudes cercanas. <br>
2.- Oferta soluciones para las necesidades a través de la creación de ofertas, y espera el contacto del productor en necesidad.

### Administrador 

1.- Visualiza a todos los usuarios registrados, con su información en la pagina. <br>
2.- Tiene la posibilidad de eliminar sus cuentas. <br>
Este usuario viene pre-registrado, y cuenta con las mismas opciones de personalización que los demás usuarios. <br>
No forma parte del flujo principal Productor-Prestador, ya que su función es mantener un control general sobre los usuarios y contribuir al correcto funcionamiento y organización del sistema.

## **Miembros**
- Tomás Aliaga (TomAlig)
- Ignacio Martinez (ignmartinezt)
- Luis San Martin (pipl3n)

## ***Commit Log** (Registro de Commits y Autoría con Descripción)*
<details>
  <summary>Haga clic aquí para desplegar Log</summary>
  <br>

  **TomAlig** - 21/09/2026 - Creación del repositorio.
  
  **pipl3n** - 01/10/2026 - Se sube la estructura del proyecto en Android Studio.

  **pipl3n** - 16/10/2026 - Creación de estructura MVVM.

  **TomAlig** - 19/10/2026 - Se sube la primera versión del README, y se desarrolla la primera versión maquetada del **Login**.

  **pipl3n, ignmartinezt** - 20/10/2026 - Se desarrollan versiones funcionales para múltiples vistas, **Crear Solicitud, Mis Solicitudes, Solicitudes Cerca**.

  **TomAlig** - 23/10/2026 / 25/10/2026 - Se realizan ajustes al maquetado inicial del Login, se desarrollan los flujos de inicio y cierre de sesión, y "Recuérdame" con Room.

  **ignmartinezt** - 26/10/2026 - Se desarrolla la vista de **Registro**, su estilizado, funcionalidades y enrutamiento.

  **TomAlig** - 28/10/2026 - Se desarrolla la vista de **Perfil** de usuario funcional y el menú de navegación para la App.

  **pipl3n, ignmartinezt** - 29/10/2026 - Se implementan la vista de **Ofertas** para el productor, **Detalle Ofertas** para el prestador, se realizan ajustes y se implementa la vista de **Admin** con su usuario correspondiente.
  
  **TomAlig** - 30/10/2026 - Se sube la versión terminada 1 del README, para la evaluación parcial 2.
