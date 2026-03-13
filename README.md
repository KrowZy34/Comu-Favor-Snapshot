# Krowdar - Conectando Talento y Comunidad

**Krowdar** es una plataforma móvil diseñada para facilitar la contratación de servicios locales e informales. Conecta de manera eficiente a **Reclutadores** (personas que necesitan un trabajo) con **Trabajadores** (personas dispuestas a ofrecer sus habilidades).

Este documento detalla el funcionamiento técnico, la arquitectura y los flujos de usuario diferenciados por roles.

---

## 🛠️ Stack Tecnológico y Arquitectura

- **Lenguaje:** Java 11 (Android SDK 36).
- **Interfaz:** XML Nativo con enfoque en un diseño premium y adaptativo.
- **Persistencia Local:** `UserPreferences` basado en `SharedPreferences` con serialización JSON para perfiles de usuario completos.
- **Transferencia de Datos:** Modelos robustos (`Job`, `Applicant`, `RecruiterJob`) que implementan `Serializable` para comunicación entre actividades.
- **UI Moderna:** Uso extensivo de `ConstraintLayout`, `EdgeToEdge`, `ShapeableImageView` y gradientes dinámicos.

---

## 👥 Experiencia del Usuario (Roles)

Tras el registro (validado por número celular) y el inicio de sesión, el usuario elige su rol. Ambos roles pasan por una pantalla de bienvenida unificada que confirma su identidad y rol actual.

### 💼 1. Flujo del Reclutador (Empleador)
El reclutador es quien crea oportunidades y gestiona el talento.

*   **Publicación de Trabajos:** Formulario completo para definir título, lugar, descripción y precio sugerido. Incluye integración con **Cámara y Galería** para subir fotos reales del sitio de trabajo.
*   **Gestión de Ofertas:** Panel para visualizar trabajos activos y aquellos con propuestas pendientes.
*   **Evaluación de Postulantes:**
    *   Lista de interesados con nombres, fotos y calificaciones simuladas.
    *   **Detalle del Aplicante:** Perfil profesional con descripción detallada y "Chips" de habilidades categorizadas por colores.
*   **Ciclo de Contratación:**
    1.  **Aceptar/Contrapropuesta:** Opciones directas para avanzar con un trabajador.
    2.  **Estado Pendiente:** Banner informativo mientras se espera la confirmación del trabajador, con opción de cancelación.
    3.  **Finalización:** Cierre formal del trabajo una vez completado.

### 🛠️ 2. Flujo del Trabajador (Empleado)
El trabajador busca generar ingresos aplicando a ofertas que coincidan con su perfil.

*   **Exploración (Feed):** Inicio dinámico con categorías (Limpieza, Reparación, etc.), trabajos destacados y recomendaciones personalizadas.
*   **Búsqueda y Filtros:** Herramientas para encontrar trabajos por preferencia o cercanía.
*   **Habilidades y Perfil:** Espacio para gestionar una descripción profesional y una lista de habilidades que los reclutadores verán al postularse.
*   **Mis Postulaciones (`Postulados`):** Seguimiento en tiempo real del estado de cada aplicación mediante tarjetas interactivas:
    *   *Propuesta enviada* (enviando/procesando).
    *   *Confirmado* (listo para iniciar).
    *   *Trabajo en proceso*.
    *   *Trabajo finalizado* (con check visual).

---

## 🏗️ Estructura del Código

### Modelos de Datos (`/model`)
- `Job`: Estructura base de una oferta laboral.
- `Applicant`: Datos del perfil del trabajador, habilidades y valoraciones.
- `RecruiterJob`: Versión extendida del trabajo para gestión administrativa del empleador.

### Adaptadores de UI (`/adapters`)
Manejan la lógica de reciclaje de vistas para un rendimiento óptimo:
- `ApplicantAdapter`: Visualización de listas de candidatos.
- `PostuladoCardAdapter`: Lógica visual para los diferentes estados de un trabajo.
- `RecruiterJobAdapter`: Tarjetas estilizadas para los trabajos publicados.

### Diseño y Estilos (`/res`)
- **`themes.xml`**: Define el sistema de diseño global (estilo circular para fotos, botones pill, etc.).
- **`colors.xml`**: Paleta curada con degradados y colores de estado (Éxito, Error, Pendiente).
- **Drawables**: Componentes reutilizables para backgrounds con esquinas redondeadas y bordes definidos.

---

## ✨ Buenas Prácticas Aplicadas

1.  **Rendimiento:** Uso estricto de `RecyclerView` con el patrón *ViewHolder* para evitar sobrecarga de memoria.
2.  **Experiencia de Usuario (UX):**
    *   Validación de campos en tiempo real (ej. el precio solo acepta números y decimales).
    *   Feedback visual inmediato mediante `Snackbars` y cambios de estado en botones.
    *   Navegación intuitiva con manejo de "Back Stack" para evitar cierres accidentales.
3.  **Premium UI:**
    *   Integración de íconos vectoriales modernos (`ic_tag_outline`, etc.).
    *   Uso de `ScrollView` con padding estratégico para evitar colisiones con elementos fijos.
    *   Tipografía limpia y jerarquía visual clara.

---

## 🚀 Cómo Empezar

1. Clona el repositorio.
2. Abre el proyecto en **Android Studio Ladybug** o superior.
3. Sincroniza con Gradle (9.2.1).
4. Ejecuta en un dispositivo con **API 24** o superior para disfrutar de la experiencia completa con Edge-to-Edge.

---
*Desarrollado con enfoque en la inclusión económica y el desarrollo comunitario.*
