const token = localStorage.getItem("jwt");

async function cargarRegistros() {
    try {
        const response = await fetch("http://localhost:8080/api/transacciones", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            console.error("Error al obtener registros:", response.status);
            return;
        }

        const registros = await response.json();
        const tbody = document.querySelector("#tabla-registros tbody");
        tbody.innerHTML = "";

        registros.forEach(registro => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${registro.id}</td>
                <td>${registro.fecha}</td>
                <td>${registro.tipo}</td>
                <td>${registro.monto}</td>
                <td>${registro.numeroCuenta || "-"}</td>
            `;
            tbody.appendChild(row);
        });

    } catch (error) {
        console.error("Error al cargar registros:", error);
    }
}

document.addEventListener("DOMContentLoaded", cargarRegistros);
