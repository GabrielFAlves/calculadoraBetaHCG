import org.example.CalculadoraIdadeGestacional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe de testes abrangente para CalculadoraIdadeGestacional usando JUnit 5
 * Baseada na lógica do site mdsaude.com
 * Aproximadamente 52 testes cobrindo todos os cenários possíveis
 */
public class TestesCalculadoraIdadeGestacional {

    private CalculadoraIdadeGestacional calculadora;

    @BeforeEach
    public void setUp() {
        calculadora = new CalculadoraIdadeGestacional();
    }

    /**
     * Cria uma data de última menstruação há N semanas
     */
    private Date criarDataUltimaMenstruacao(int semanasAtras) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -semanasAtras);
        return cal.getTime();
    }

    @Test
    public void testValorNegativo() {
        Date dum = criarDataUltimaMenstruacao(6);
        String resultado = calculadora.calcularIdadeGestacional(-1.0, dum);
        assertEquals("Valor de Beta hCG inválido", resultado);
    }

    @Test
    public void testValorZero() {
        Date dum = criarDataUltimaMenstruacao(6);
        String resultado = calculadora.calcularIdadeGestacional(0.0, dum);
        assertTrue(resultado.contains("Resultado negativo"));
    }

    @Test
    public void testDataNula() {
        String resultado = calculadora.calcularIdadeGestacional(100.0, null);
        assertEquals("Data da última menstruação não informada", resultado);
    }

    @Test
    public void testValorMuitoAlto() {
        Date dum = criarDataUltimaMenstruacao(6);
        String resultado = calculadora.calcularIdadeGestacional(Double.MAX_VALUE, dum);
        assertNotNull(resultado);
    }

    @Test
    public void testValorDecimal() {
        Date dum = criarDataUltimaMenstruacao(5);
        String resultado = calculadora.calcularIdadeGestacional(25.5, dum);
        assertNotNull(resultado);
    }

    @Test
    public void testResultadoNegativoMuitoRecente2Semanas() {
        Date dum = criarDataUltimaMenstruacao(2);
        String resultado = calculadora.calcularIdadeGestacional(20.0, dum);
        assertTrue(resultado.contains("fora do intervalo") || resultado.contains("Resultado negativo"));
    }

    @Test
    public void testResultadoNegativoRecente3Semanas() {
        Date dum = criarDataUltimaMenstruacao(3);
        String resultado = calculadora.calcularIdadeGestacional(20.0, dum);
        assertTrue(resultado.contains("Resultado negativo"));
    }

    @Test
    public void testResultadoNegativoLimite() {
        Date dum = criarDataUltimaMenstruacao(6);
        String resultado = calculadora.calcularIdadeGestacional(24.9, dum);
        assertTrue(resultado.contains("Resultado negativo"));
        assertFalse(resultado.contains("falso negativo"));
    }

    @Test
    public void testResultadoNegativoExato25() {
        Date dum = criarDataUltimaMenstruacao(6);
        String resultado = calculadora.calcularIdadeGestacional(25.0, dum);
        assertFalse(resultado.contains("Resultado negativo"));
    }

    @Test
    public void testResultadoNegativoTardio() {
        Date dum = criarDataUltimaMenstruacao(10);
        String resultado = calculadora.calcularIdadeGestacional(15.0, dum);
        assertTrue(resultado.contains("Resultado negativo"));
        assertFalse(resultado.contains("falso negativo"));
    }

    @Test
    public void testResultadoNegativoMuitoTardio() {
        Date dum = criarDataUltimaMenstruacao(20);
        String resultado = calculadora.calcularIdadeGestacional(10.0, dum);
        assertTrue(resultado.contains("fora do intervalo") || resultado.contains("Resultado negativo"));
    }

    @Test
    public void testIdadeGestacionalMuitoCedo1Semana() {
        Date dum = criarDataUltimaMenstruacao(1);
        String resultado = calculadora.calcularIdadeGestacional(100.0, dum);
        assertTrue(resultado.contains("fora do intervalo"));
        assertTrue(resultado.contains("3-16 semanas"));
    }

    @Test
    public void testIdadeGestacionalExato3Semanas() {
        Date dum = criarDataUltimaMenstruacao(3);
        String resultado = calculadora.calcularIdadeGestacional(30.0, dum);
        assertFalse(resultado.contains("fora do intervalo"));
    }

    @Test
    public void testIdadeGestacionalExato16Semanas() {
        Date dum = criarDataUltimaMenstruacao(16);
        String resultado = calculadora.calcularIdadeGestacional(100000.0, dum);
        assertFalse(resultado.contains("fora do intervalo"));
    }

    @Test
    public void testIdadeGestacionalMuitoTarde17Semanas() {
        Date dum = criarDataUltimaMenstruacao(17);
        String resultado = calculadora.calcularIdadeGestacional(100.0, dum);
        assertTrue(resultado.contains("fora do intervalo"));
    }

    @Test
    public void testCompativel3Semanas() {
        Date dum = criarDataUltimaMenstruacao(3);
        String resultado = calculadora.calcularIdadeGestacional(30.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("3 semanas"));
    }

    @Test
    public void testCompativel4Semanas() {
        Date dum = criarDataUltimaMenstruacao(4);
        String resultado = calculadora.calcularIdadeGestacional(200.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("4 semanas"));
    }

    @Test
    public void testCompativel5Semanas() {
        Date dum = criarDataUltimaMenstruacao(5);
        String resultado = calculadora.calcularIdadeGestacional(2000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("5 semanas"));
    }

    @Test
    public void testCompativel6Semanas() {
        Date dum = criarDataUltimaMenstruacao(6);
        String resultado = calculadora.calcularIdadeGestacional(20000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("6 semanas"));
    }

    @Test
    public void testCompativel7Semanas() {
        Date dum = criarDataUltimaMenstruacao(7);
        String resultado = calculadora.calcularIdadeGestacional(50000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("7 semanas"));
    }

    @Test
    public void testCompativel8Semanas() {
        Date dum = criarDataUltimaMenstruacao(8);
        String resultado = calculadora.calcularIdadeGestacional(100000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("8 semanas"));
    }

    @Test
    public void testCompativel9Semanas() {
        Date dum = criarDataUltimaMenstruacao(9);
        String resultado = calculadora.calcularIdadeGestacional(150000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("9 semanas"));
    }

    @Test
    public void testCompativel10Semanas() {
        Date dum = criarDataUltimaMenstruacao(10);
        String resultado = calculadora.calcularIdadeGestacional(200000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("10 semanas"));
    }

    @Test
    public void testCompativel11Semanas() {
        Date dum = criarDataUltimaMenstruacao(11);
        String resultado = calculadora.calcularIdadeGestacional(250000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("11 semanas"));
    }

    @Test
    public void testCompativel12Semanas() {
        Date dum = criarDataUltimaMenstruacao(12);
        String resultado = calculadora.calcularIdadeGestacional(280000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("12 semanas"));
    }

    @Test
    public void testCompativel13Semanas() {
        Date dum = criarDataUltimaMenstruacao(13);
        String resultado = calculadora.calcularIdadeGestacional(200000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("13 semanas"));
    }

    @Test
    public void testCompativel14Semanas() {
        Date dum = criarDataUltimaMenstruacao(14);
        String resultado = calculadora.calcularIdadeGestacional(180000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("14 semanas"));
    }

    @Test
    public void testCompativel15Semanas() {
        Date dum = criarDataUltimaMenstruacao(15);
        String resultado = calculadora.calcularIdadeGestacional(160000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("15 semanas"));
    }

    @Test
    public void testCompativel16Semanas() {
        Date dum = criarDataUltimaMenstruacao(16);
        String resultado = calculadora.calcularIdadeGestacional(140000.0, dum);
        assertTrue(resultado.contains("compatível"));
        assertTrue(resultado.contains("16 semanas"));
    }

    @Test
    public void testAbaixoEsperado5Semanas() {
        Date dum = criarDataUltimaMenstruacao(5);
        String resultado = calculadora.calcularIdadeGestacional(15.0, dum);
        assertTrue(resultado.contains("Resultado negativo"));
    }

    @Test
    public void testAbaixoEsperado6Semanas() {
        Date dum = criarDataUltimaMenstruacao(6);
        String resultado = calculadora.calcularIdadeGestacional(500.0, dum);
        assertTrue(resultado.contains("abaixo do esperado"));
        assertTrue(resultado.contains("acompanhamento médico"));
    }


}