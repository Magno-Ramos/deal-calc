// Tab switching functionality
document.getElementById('tab1').addEventListener('click', function () {
    switchTab(1);
});

document.getElementById('tab2').addEventListener('click', function () {
    switchTab(2);
});

function switchTab(tabNumber) {
    // Reset tab styles
    document.getElementById('tab1').className = 'flex-1 py-3 px-4 rounded-lg font-medium transition-all duration-200 text-gray-600 hover:text-black';
    document.getElementById('tab2').className = 'flex-1 py-3 px-4 rounded-lg font-medium transition-all duration-200 text-gray-600 hover:text-black';

    // Hide all calculators
    document.getElementById('calc1').classList.add('hidden');
    document.getElementById('calc2').classList.add('hidden');

    // Show selected tab and calculator
    if (tabNumber === 1) {
        document.getElementById('tab1').className = 'flex-1 py-3 px-4 rounded-lg font-medium transition-all duration-200 bg-black text-white';
        document.getElementById('calc1').classList.remove('hidden');
    } else {
        document.getElementById('tab2').className = 'flex-1 py-3 px-4 rounded-lg font-medium transition-all duration-200 bg-black text-white';
        document.getElementById('calc2').classList.remove('hidden');
    }

    // Hide results when switching tabs
    document.getElementById('result1').classList.add('hidden');
    document.getElementById('result2').classList.add('hidden');
}

// Calculate percentage discount
function calculatePercentage() {
    const originalValue = parseFloat(document.getElementById('originalValue1').value);
    const discountedValue = parseFloat(document.getElementById('discountedValue').value);

    if (!originalValue || !discountedValue) {
        alert('Por favor, preencha todos os campos com valores válidos.');
        return;
    }

    if (discountedValue > originalValue) {
        alert('O valor com desconto não pode ser maior que o valor original.');
        return;
    }

    const discountAmount = originalValue - discountedValue;
    const discountPercentage = (discountAmount / originalValue) * 100;

    document.getElementById('percentageResult').textContent = discountPercentage.toFixed(1) + '%';
    document.getElementById('savingsAmount').textContent = 'R$ ' + discountAmount.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    document.getElementById('result1').classList.remove('hidden');
}

// Calculate final value with discount
function calculateFinalValue() {
    const originalValue = parseFloat(document.getElementById('originalValue2').value);
    const discountPercentage = parseFloat(document.getElementById('discountPercentage').value);

    if (!originalValue || discountPercentage === null || discountPercentage === undefined) {
        alert('Por favor, preencha todos os campos com valores válidos.');
        return;
    }

    if (discountPercentage < 0 || discountPercentage > 100) {
        alert('O percentual de desconto deve estar entre 0% e 100%.');
        return;
    }

    const discountAmount = (originalValue * discountPercentage) / 100;
    const finalValue = originalValue - discountAmount;

    document.getElementById('finalValueResult').textContent = 'R$ ' + finalValue.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    document.getElementById('savingsAmount2').textContent = 'R$ ' + discountAmount.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    document.getElementById('result2').classList.remove('hidden');
}

// Add Enter key support for inputs
document.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        const activeTab = document.getElementById('calc1').classList.contains('hidden') ? 2 : 1;
        if (activeTab === 1) {
            calculatePercentage();
        } else {
            calculateFinalValue();
        }
    }
});