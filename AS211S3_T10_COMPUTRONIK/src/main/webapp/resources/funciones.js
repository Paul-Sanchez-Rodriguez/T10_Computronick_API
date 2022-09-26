
console.log("Si entro ");

function visible() {
    let selec = document.getElementById("filtro");
    let nueva = selec.value;
    if (nueva === 3) {
        alert("funciona");
    }
}

function mostrar() {
    let mostrar = document.getElementById('prueba').style.display = 'block';
    let comprar = document.getElementById('nutton');
    console.log(compra)

}

function activarinput() {
    
    document.getElementById('txtapellido').disabled = false;   
}
            