let clienteId = null;
const token = localStorage.getItem("jwt");

async function cargarCuentasCliente() {
    const token = localStorage.getItem("jwt");

    try {
        const response = await fetch("http://localhost:8080/api/cuentas/mis-cuentas", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("No se pudieron obtener las cuentas del cliente");
        }

        const cuentas = await response.json();
        const container = document.getElementById("cuentas-container");
        container.innerHTML = "";

        cuentas.forEach(cuenta => {
            const div = document.createElement("div");
            div.classList.add("cuenta");
            div.innerHTML = `
                <p><strong>Numero de Cuenta:</strong> ${cuenta.numeroCuenta}</p>
                <p><strong>Saldo:</strong> $${cuenta.saldo.toFixed(2)}</p>
                <button onclick="verTransacciones(${cuenta.id})">Ver Transacciones</button>
            `;
            container.appendChild(div);
        });
    } catch (error) {
        console.error(error.message);
    }
}

async function verTransacciones(cuentaId) {
    const token = localStorage.getItem("jwt");
    const existingContainer = document.getElementById("transacciones-container-" + cuentaId);

    if (existingContainer) {
        existingContainer.remove();
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/transacciones/cuenta/${cuentaId}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("No se pudieron obtener las transacciones.");
        }

        const transacciones = await response.json();

        const contenedor = document.createElement("div");
        contenedor.id = "transacciones-container-" + cuentaId;
        contenedor.style.marginTop = "15px";
        contenedor.style.padding = "10px";
        contenedor.style.border = "1px solid #ccc";
        contenedor.style.borderRadius = "8px";
        contenedor.style.backgroundColor = "#f8f9fa";

        let html = `<h4>Transacciones de la cuenta ${cuentaId}</h4>`;

        if (transacciones.length === 0) {
            html += `<p>No hay transacciones registradas.</p>`;
        } else {
            html += `<table style="width:100%; border-collapse: collapse;">
                        <thead>
                            <tr style="background-color: #dee2e6;">
                                <th style="border: 1px solid #ccc; padding: 8px;">Tipo</th>
                                <th style="border: 1px solid #ccc; padding: 8px;">Monto</th>
                                <th style="border: 1px solid #ccc; padding: 8px;">Fecha</th>
                            </tr>
                        </thead>
                        <tbody>`;

            transacciones.forEach(tx => {
                const fecha = new Date(tx.fecha).toLocaleString();
                html += `
                    <tr>
                        <td style="border: 1px solid #ccc; padding: 8px;">${tx.tipo}</td>
                        <td style="border: 1px solid #ccc; padding: 8px;">$${tx.monto.toFixed(2)}</td>
                        <td style="border: 1px solid #ccc; padding: 8px;">${fecha}</td>
                    </tr>`;
            });

            html += `</tbody></table>`;
        }

        contenedor.innerHTML = html;

        const boton = document.querySelector(`button[onclick="verTransacciones(${cuentaId})"]`);
        boton.parentNode.insertBefore(contenedor, boton.nextSibling);

    } catch (error) {
        console.error("Error obteniendo transacciones:", error);
    }
}


async function cargarCliente() {
    const token = localStorage.getItem("jwt");

    try {
        const response = await fetch("http://localhost:8080/api/clientes/yo", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) throw new Error("No se pudo obtener el cliente");

        const cliente = await response.json();
        clienteId = cliente.id;
        document.getElementById("cliente-nombre").innerText = cliente.nombre;

        await mostrarBalanceTotal();

    } catch (error) {
        console.error("Error cargando datos del cliente:", error.message);
    }
}

async function mostrarBalanceTotal() {
    const token = localStorage.getItem("jwt");

    try {
        const response = await fetch("http://localhost:8080/api/clientes/yo/balance", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("No se pudo obtener el balance");
        }

        const balance = await response.json();
        console.log("Balance recibido:", balance);
        document.getElementById("balance-total").innerText = `Balance total: $${balance.toFixed(2)}`;
    } catch (error) {
        console.error("Error mostrando balance:", error);
    }
}


document.addEventListener("DOMContentLoaded", () => {
    cargarCliente();           
    cargarCuentasCliente();   
});
