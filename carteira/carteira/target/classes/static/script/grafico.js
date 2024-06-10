const ctx = document.getElementById('myChart').getContext('2d');
const gastos = [[${gastos}]]
const receitas = [[${receitas}]];


const data = {
    labels: ['Gastos', 'Receitas'], // Labels para os dados
    datasets: [
        {
            label: 'Valor',
            data: [gastos, receitas], // Dados de gastos e receitas
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)', // Cor para gastos (vermelho)
                'rgba(75, 192, 192, 0.2)' // Cor para receitas (verde)
            ],
            borderColor: [
                'rgba(255, 99, 132, 1)', // Cor da borda para gastos (vermelho)
                'rgba(75, 192, 192, 1)' // Cor da borda para receitas (verde)
            ],
            borderWidth: 1
        }
    ]
};

const myChart = new Chart(ctx, {
    type: 'bar',
    data: data,
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});