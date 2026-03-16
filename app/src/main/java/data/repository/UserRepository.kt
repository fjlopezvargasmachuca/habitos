package data.repository

import com.example.habitos.data.entities.User
import com.example.habitos.data.dao.UserDao

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con los usuarios de la aplicación.
 *
 * Sus principales responsabilidades son:
 *
 * - Gestionar la autenticación de usuarios.
 * - Registrar nuevos usuarios en el sistema.
 * - Obtener listados de usuarios para funcionalidades como ranking o moderación.
 * - Aplicar acciones administrativas como banear o desbanear usuarios.
 * - Gestionar la puntuación de los usuarios.
 *
 */
class UserRepository(private val dao: UserDao) {

    /**
     * Obtener un usuario a partir de su correo electrónico.
     *
     * @param email Dirección de correo del usuario.
     * @return Objeto `User` si el usuario existe, o null en caso contrario.
     */
    suspend fun login(email: String): User? =
        dao.obtenerPorEmail(email)

    /**
     * Registrar un nuevo usuario en la base de datos.
     *
     * @param user Objeto `User` con la información del nuevo usuario.
     * @return Usuario registrado con su identificador asignado.
     */
    suspend fun registrar(user: User): User {
        val id = dao.insertar(user).toInt()
        return dao.obtenerPorId(id)!!
    }

    /**
     * Obtener todos los usuarios registrados en la aplicación.
     *
     * @return Lista de usuarios ordenados según la consulta definida en el DAO.
     */
    suspend fun obtenerTodos(): List<User> =
        dao.obtenerTodos()

    /**
     * Marcar un usuario como baneado dentro del sistema.
     *
     * @param id Identificador del usuario.
     */
    suspend fun banear(id: Int) =
        dao.banear(id)

    /**
     * Revertir el estado de 'baneo' de un usuario.
     *
     * @param id Identificador del usuario.
     */
    suspend fun desbanear(id: Int) =
        dao.desbanear(id)

    /**
     * Añadir puntos al contador total del usuario.
     *
     * @param id Identificador del usuario.
     * @param puntos Cantidad de puntos a añadir.
     */
    suspend fun sumarPuntos(id: Int, puntos: Int) =
        dao.sumarPuntos(id, puntos)
}