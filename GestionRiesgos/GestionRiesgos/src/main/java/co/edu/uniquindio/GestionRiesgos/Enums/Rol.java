package co.edu.uniquindio.GestionRiesgos.Enums;

    /**
     * Enum que representa los roles de usuario en el sistema
     */
    public enum Rol {
        ADMINISTRADOR("Administrador", 1),
        OPERADOR_EMERGENCIA("Operador de Emergencia", 2);

        private final String descripcion;
        private final int nivelAcceso;

        Rol(String descripcion, int nivelAcceso) {
            this.descripcion = descripcion;
            this.nivelAcceso = nivelAcceso;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public int getNivelAcceso() {
            return nivelAcceso;
        }

        /**
         * Verifica si el rol tiene permisos de administrador
         */
        public boolean esAdministrador() {
            return this == ADMINISTRADOR;
        }

        /**
         * Verifica si el rol puede gestionar recursos
         */
        public boolean puedeGestionarRecursos() {
            return this == ADMINISTRADOR || this == OPERADOR_EMERGENCIA;
        }

        /**
         * Verifica si el rol puede ver estadísticas
         */
        public boolean puedeVerEstadisticas() {
            return this == ADMINISTRADOR || this == OPERADOR_EMERGENCIA;
        }
    }

