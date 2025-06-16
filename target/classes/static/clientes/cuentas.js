// clientes/cuentas.js

const token = localStorage.getItem("jwt");
const payload = JSON.parse(atob(token.split('.')[1]));
const email = payload.sub;

let clienteId;

const tablaBody = document.querySelector("#tabla-cuentas tbody");
const formCrearCuenta = document.getElementById("form-crear-cuenta");

async function obtenerClienteIdPorEmail() {
    try {
        const response = await fetch("http://localhost:8080/api/clientes", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            alert("No se pudieron cargar los clientes");
            return;
        }

        const clientes = await response.json();
        const cliente = clientes.find(c => c.usuario.email === email);

        if (cliente) {
            clienteId = cliente.usuario.id;
            cargarCuentas();
        } else {
            alert("Cliente no encontrado");
        }

    } catch (error) {
        console.error("Error obteniendo cliente por email:", error);
    }
}

async function cargarCuentas() {
    try {
        const response = await fetch(`http://localhost:8080/api/clientes/${clienteId}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            alert("Error al cargar cuentas.");
            return;
        }

        const cliente = await response.json();
        const cuentas = cliente.cuentas || [];

        tablaBody.innerHTML = "";
cuentas.forEach(cuenta => {
    const fila = document.createElement("tr");
    fila.innerHTML = `
        <td>${cuenta.numeroCuenta}</td>
        <td>${cuenta.tipo}</td>
        <td>$${cuenta.saldo.toFixed(2)}</td>
        <td>
            <button onclick="eliminarCuenta(${cuenta.id})">Eliminar</button>
            <button onclick="mostrarFormulario('depositar', ${cuenta.id})">Depositar</button>
            <button onclick="mostrarFormulario('retirar', ${cuenta.id})">Retirar</button>
        </td>
    `;
    tablaBody.appendChild(fila);
});
    } catch (error) {
        console.error("Error al obtener cuentas:", error);
    }
}

async function crearCuenta(event) {
    event.preventDefault();

    const tipo = document.getElementById("tipo").value;
    if (!tipo || !clienteId) return;

    const cuenta = {
        tipo: tipo,
        saldo: 0
    };

    try {
        const response = await fetch(`http://localhost:8080/api/clientes/${clienteId}/cuentas`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(cuenta)
        });

        if (!response.ok) {
            const errorText = await response.text();
            alert("Error al crear cuenta: " + errorText);
            return;
        }

        formCrearCuenta.reset();
        cargarCuentas();
    } catch (error) {
        console.error("Error al crear cuenta:", error);
    }
}

async function eliminarCuenta(cuentaId) {
    if (!confirm("¿Estás seguro de eliminar esta cuenta?")) return;

    try {
        const response = await fetch(`http://localhost:8080/api/clientes/${clienteId}/cuentas/${cuentaId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) {
            cargarCuentas();
        } else {
            const errorText = await response.text();
            alert("Error al eliminar cuenta: " + errorText);
        }
    } catch (error) {
        console.error("Error eliminando cuenta:", error);
    }
}

let cuentaIdSeleccionada = null;
let accionSeleccionada = null;

function mostrarFormulario(accion, cuentaId) {
    cuentaIdSeleccionada = cuentaId;
    accionSeleccionada = accion;

    document.getElementById("formulario-monto").style.display = "block";
    document.getElementById("form-title").innerText = accion === "depositar" ? "Depositar monto" : "Retirar monto";
}

function ejecutarAccion() {
    const monto = parseFloat(document.getElementById("monto").value);

    if (!monto || monto <= 0) {
        alert("Ingresa un monto válido.");
        return;
    }

    const url = `http://localhost:8080/api/cuentas/${cuentaIdSeleccionada}/${accionSeleccionada}`;
    const body = { monto };

    fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(body)
    })
    .then(response => {
        if (!response.ok) throw new Error("Error en la transacción.");
        return response.json();
    })
    .then(() => {
        alert(`${accionSeleccionada === "depositar" ? "Depósito" : "Retiro"} exitoso`);
        location.reload();
    })
    .catch(error => alert(error.message));
}



formCrearCuenta.addEventListener("submit", crearCuenta);
obtenerClienteIdPorEmail();
