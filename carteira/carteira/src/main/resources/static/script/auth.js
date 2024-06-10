
function mostrarCadastro() {
    document.getElementById('erroLogin').classList.add('hidden');
    document.getElementById('register').classList.remove('hidden');
    document.getElementById('login').classList.add('hidden');
}

function mostrarLogin() {
    document.getElementById('erroSenha').classList.add('hidden');
    document.getElementById('login').classList.remove('hidden');
    document.getElementById('register').classList.add('hidden');
}

function validarSenha() {
    const senha= document.getElementById('senhar').value;
    const confirmar= document.getElementById('confirmarSenha').value;

    if (senha !== confirmar) {
        document.getElementById('erroSenha').classList.remove('hidden');
        return false;
    }
    return true;
}