const token = localStorage.getItem("jwt");
let clientes = [];

function crearFilaCliente(cliente) {
    const fila = document.createElement("tr");

    let totalBalance = cliente.balance ?? 0;

    fila.innerHTML = `
        <td>${cliente.usuario?.id ?? "-"}</td>
        <td>${cliente.usuario?.nombre ?? "-"}</td>
        <td>${cliente.usuario?.email ?? "-"}</td>
        <td>${cliente.usuario?.telefono ?? "-"}</td>
        <td>
            ${cliente.cuentas?.length > 0
                ? cliente.cuentas.map(cuenta =>
                    `<div>
                        <strong>${cuenta.numeroCuenta}</strong> - <strong>${cuenta.tipo}</strong>: $${cuenta.saldo.toFixed(2)}
                        <button onclick="eliminarCuenta(${cuenta.id}, ${cliente.usuario?.id})">Eliminar</button>
                    </div>`).join("")
                : "-"}
        </td>
        <td><strong>$${totalBalance.toFixed(2)}</strong></td>
        <td>
            <button onclick="editarCliente(${cliente.usuario?.id})">✎</button>
        </td>
    `;
    return fila;
}

async function cargarClientes() {
    const tablaBody = document.getElementById("tabla-clientes-body");
    tablaBody.innerHTML = "";

    try {
        const respuesta = await fetch("http://localhost:8080/api/clientes", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        clientes = await respuesta.json();

        for (const cliente of clientes) {
            const fila = crearFilaCliente(cliente);
            tablaBody.appendChild(fila);
        }

    } catch (error) {
        console.error("Error al cargar los clientes:", error);
    }
}

function editarCliente(id) {
    const cliente = clientes.find(c => c.usuario?.id === id);

    if (cliente) {
        mostrarFormularioEditar(cliente.usuario.id, cliente.usuario.nombre, cliente.usuario.email, cliente.usuario.telefono);
    } else {
        console.error("Cliente no encontrado");
    }
}

function mostrarFormularioEditar(id, nombre, email, telefono) {
    document.getElementById("editar-id").value = id;
    document.getElementById("editar-nombre").value = nombre;
    document.getElementById("editar-email").value = email;
    document.getElementById("editar-telefono").value = telefono;
    document.getElementById("editar-cliente-form").style.display = "block";
}

document.getElementById("editar-cliente-form").addEventListener("submit", async (event) => {
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
            document.getElementById("editar-cliente-form").style.display = "none";
            cargarClientes();
        } else {
            const error = await response.text();
            alert("❌ Error al actualizar: " + error);
        }

    } catch (error) {
        console.error("Error al actualizar:", error);
        alert("❌ Error de red");
    }
});

async function eliminarCuenta(cuentaId, clienteId) {
    if (!confirm("¿Eliminar esta cuenta?")) return;

    try {
        const res = await fetch(`http://localhost:8080/api/clientes/${clienteId}/cuentas/${cuentaId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (res.ok) {
            alert("✅ Cuenta eliminada correctamente");
            cargarClientes();
        } else {
            let mensajeError;

            try {
                const data = await res.json(); 
                mensajeError = data.message || JSON.stringify(data);
            } catch (e) {
                mensajeError = await res.text(); 
            }

            alert("❌ Error: " + mensajeError);
        }

    } catch (err) {
        console.error("Error al eliminar cuenta:", err);
        alert("❌ Error de red al intentar eliminar la cuenta");
    }
}

document.getElementById("crear-cliente-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = event.target;

    const nuevoCliente = {
        nombre: form.nombre.value,
        email: form.email.value,
        contraseña: form.contraseña.value,
        telefono: form.telefono.value,
        tipo_usuario: "CLIENTE"
    };

    try {
        const res = await fetch("http://localhost:8080/api/usuarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(nuevoCliente)
        });

        if (res.ok) {
            form.reset();
            cargarClientes();
        } else {
            alert("❌ Error al crear cliente");
        }
    } catch (err) {
        console.error("Error al crear cliente:", err);
    }
});

document.getElementById("buscar-btn").addEventListener("click", async () => {
    const id = document.getElementById("buscar-id").value;
    if (!id) return cargarClientes();

    try {
        const res = await fetch(`http://localhost:8080/api/clientes/${id}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!res.ok) {
            alert("Cliente no encontrado");
            return;
        }

        const cliente = await res.json();

        const tbody = document.querySelector("#tabla-clientes tbody");
        tbody.innerHTML = "";

        const fila = crearFilaCliente(cliente);
        tbody.appendChild(fila);

    } catch (err) {
        console.error("Error al buscar cliente:", err);
    }
});

document.addEventListener("DOMContentLoaded", cargarClientes);
