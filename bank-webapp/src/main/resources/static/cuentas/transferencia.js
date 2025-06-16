document.addEventListener("DOMContentLoaded", async () => {
    await cargarCuentasDelCliente();

    document.getElementById("form-transferencia").addEventListener("submit", async (e) => {
        e.preventDefault();
        await realizarTransferencia();
    });
});

async function cargarCuentasDelCliente() {
    const token = localStorage.getItem("jwt");
    try {
        const response = await fetch("http://localhost:8080/api/cuentas/mis-cuentas", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("No se pudieron obtener las cuentas");
        }

        const cuentas = await response.json();
        const select = document.getElementById("cuentaOrigen");
        select.innerHTML = "";

        cuentas.forEach(cuenta => {
            const option = document.createElement("option");
            option.value = cuenta.id;
            option.textContent = `Cuenta ${cuenta.numeroCuenta} - $${cuenta.saldo.toFixed(2)}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error("Error cargando cuentas:", error);
    }
}

async function realizarTransferencia() {
    const token = localStorage.getItem("jwt");
    const cuentaOrigenId = document.getElementById("cuentaOrigen").value;
    const numeroCuentaDestinatario = document.getElementById("destinatario").value;
    const monto = parseFloat(document.getElementById("monto").value);

    const mensajeDiv = document.getElementById("mensaje");
    mensajeDiv.innerText = "";

    try {
        const response = await fetch("http://localhost:8080/api/transacciones/transferir", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({
                cuentaOrigenId,
                numeroCuentaDestinatario,
                monto
            })
        });

        const resultado = await response.json();

        if (!response.ok) {
            mensajeDiv.innerText = "Error: " + (resultado.error || "Error desconocido");
            mensajeDiv.style.color = "red";
            return;
        }

        mensajeDiv.innerText = `Transferencia realizada con éxito.\nDe: ${resultado.numeroCuentaOrigen}\nA: ${resultado.numeroCuentaDestino}`;
        mensajeDiv.style.color = "green";

        setTimeout(() => {
            window.location.href = "../cliente.html";
        }, 1000);

    } catch (error) {
        console.error("Error realizando transferencia:", error);
        mensajeDiv.innerText = "Error de red o del servidor.";
        mensajeDiv.style.color = "red";
    }

}



