package ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.habitos.data.enums.Provincia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarPerfil(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val usuario by authViewModel.usuarioActual.collectAsState()

    if (usuario == null) return

    var nick by remember { mutableStateOf(usuario!!.nick) }
    var provincia by remember { mutableStateOf(usuario!!.provincia) }

    /**
     * Control del menú desplegable de provincias
     */
    var provinciaExpanded by remember { mutableStateOf(false) }

    /**
     * Lista de provincias obtenidas del enum
     */
    val provincias = Provincia.values()

    /**
     * Lista de usuarios para comprobar duplicados de nick
     */
    val usuarios by authViewModel.usuarios.collectAsState(initial = emptyList())

    Scaffold(

        topBar = {

            TopAppBar(

                title = { Text("Editar perfil") },

                navigationIcon = {

                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            OutlinedTextField(

                value = nick,

                onValueChange = { nick = it },

                label = { Text("Nick") },

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            /**
             * SELECTOR DE PROVINCIA (DESPLEGABLE)
             */
            ExposedDropdownMenuBox(
                expanded = provinciaExpanded,
                onExpandedChange = { provinciaExpanded = !provinciaExpanded }
            ) {

                OutlinedTextField(
                    value = provincia,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Provincia") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(provinciaExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = provinciaExpanded,
                    onDismissRequest = { provinciaExpanded = false }
                ) {

                    provincias.forEach {

                        DropdownMenuItem(
                            text = { Text(it.nombre) },
                            onClick = {
                                provincia = it.nombre
                                provinciaExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(

                onClick = {

                    val nickExiste = usuarios.any {
                        it.nick.equals(nick, ignoreCase = true) &&
                                it.id != usuario!!.id
                    }

                    if (nickExiste) {

                        Toast.makeText(
                            navController.context,
                            "Ese nick ya existe. Intenta con otro.",
                            Toast.LENGTH_LONG
                        ).show()

                        return@Button
                    }

                    authViewModel.actualizarPerfil(
                        usuario!!.id,
                        nick,
                        provincia
                    )

                    navController.popBackStack()
                },

                modifier = Modifier.fillMaxWidth()

            ) {

                Text("Guardar cambios")
            }
        }
    }
}