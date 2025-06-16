const token = localStorage.getItem("jwt");

async function cargarUsuarios() {
    try {
        const response = await fetch("http://localhost:8080/api/usuarios", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            console.error("Error al obtener usuarios:", response.status);
            return;
        }

        const usuarios = await response.json();
        const tbody = document.querySelector("#tabla-usuarios tbody");
        tbody.innerHTML = "";

        usuarios.forEach(usuario => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${usuario.id}</td>
    <td>${usuario.nombre}</td>
    <td>${usuario.email}</td>
    <td>${usuario.telefono}</td>
    <td>${usuario.tipo_usuario}</td>
    <td>
        <button onclick="eliminarUsuario(${usuario.id})">Eliminar</button>
        <button onclick='editarUsuario(${JSON.stringify(usuario)})'>Editar</button>
    </td>
            `;
            tbody.appendChild(row);
        });

    } catch (error) {
        console.error("Error al cargar usuarios:", error);
    }
}

function mostrarFormularioEdicion(usuario) {
    document.getElementById("editar-id").value = usuario.id;
    document.getElementById("editar-nombre").value = usuario.nombre;
    document.getElementById("editar-email").value = usuario.email;
    document.getElementById("editar-telefono").value = usuario.telefono;
    document.getElementById("editar-usuario-form").style.display = "block";
}

async function buscarUsuarioPorId() {
    const idInput = document.getElementById("buscar-id");
    const id = idInput.value.trim();

    if (id === "") {
        cargarUsuarios(); 
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/usuarios/${id}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            alert("Usuario no encontrado");
            cargarUsuarios(); 
            return;
        }

        const usuario = await response.json();
        const tbody = document.querySelector("#tabla-usuarios tbody");
        tbody.innerHTML = `
            <tr>
                <td>${usuario.id}</td>
                <td>${usuario.nombre}</td>
                <td>${usuario.email}</td>
                <td>${usuario.telefono}</td>
                <td>${usuario.tipo_usuario}</td>
                <td>
                    <button onclick="eliminarUsuario(${usuario.id})">Eliminar</button>
                    <button onclick='editarUsuario(${JSON.stringify(usuario)})'>Editar</button>
                </td>
            </tr>
        `;

    } catch (error) {
        console.error("Error al buscar usuario:", error);
    }
}

function editarUsuario(usuario) {
    mostrarFormularioEdicion(usuario);
}

async function eliminarUsuario(id) {
    if (!confirm("¿Estás seguro de eliminar este usuario?")) return;

    try {
        const response = await fetch(`http://localhost:8080/api/usuarios/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) {
            console.log("Usuario eliminado");
            cargarUsuarios();
        } else {
            console.warn("Error al eliminar usuario:", response.status);
        }

    } catch (error) {
        console.error("Error al eliminar:", error);
    }
}

document.getElementById("crear-usuario-form").addEventListener("submit", async (event) => {
    event.preventDefault();

    const form = event.target;

    const nuevoUsuario = {
        nombre: form.nombre.value,
        email: form.email.value,
        contraseña: form.contraseña.value,
        telefono: form.telefono.value,
        tipo_usuario: form.tipo_usuario.value
    };

    try {
        const response = await fetch("http://localhost:8080/api/usuarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(nuevoUsuario)
        });

        if (response.ok) {
            alert("✅ Usuario creado con éxito.");
            form.reset();
            cargarUsuarios();
        } else {
            const error = await response.text();
            alert("❌ Error al crear usuario: " + error);
        }
    } catch (error) {
        console.error("❌ Error de red:", error);
        alert("❌ No se pudo conectar con el servidor.");
    }
});

document.getElementById("editar-usuario-form").addEventListener("submit", async (event) => {
    event.preventDefault();

    const id = document.getElementById("editar-id").value;
    const usuarioEditado = {
        nombre: document.getElementById("editar-nombre").value,
        email: document.getElementById("editar-email").value,
        telefono: document.getElementById("editar-telefono").value,
    };

    try {
        const response = await fetch(`http://localhost:8080/api/usuarios/usuarios/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(usuarioEditado)
        });

        if (response.ok) {
            alert("✅ Usuario actualizado");
            document.getElementById("editar-usuario-form").style.display = "none";
            cargarUsuarios();
        } else {
            const error = await response.text();
            alert("❌ Error al actualizar: " + error);
        }

    } catch (error) {
        console.error("Error al actualizar:", error);
        alert("❌ Error de red");
    }
});

document.getElementById("buscar-btn").addEventListener("click", buscarUsuarioPorId);
document.addEventListener("DOMContentLoaded", cargarUsuarios);
