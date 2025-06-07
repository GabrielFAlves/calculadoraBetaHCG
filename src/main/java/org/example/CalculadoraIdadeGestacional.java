package org.example;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Classe para calcular a idade gestacional baseada nos valores de Beta hCG
 * e data da última menstruação, seguindo a lógica do site mdsaude.com
 */
public class CalculadoraIdadeGestacional {

    /**
     * Valores de referência do Beta hCG por semana gestacional (em mUI/mL)
     * Baseado nas faixas padrão da literatura médica
     */
    private static final double[][] FAIXAS_BETA_HCG = {
            {3, 5, 50},      // 3 semanas
            {4, 5, 426},     // 4 semanas
            {5, 18, 7340},   // 5 semanas
            {6, 1080, 56500}, // 6 semanas
            {7, 7650, 229000}, // 7 semanas
            {8, 7650, 229000}, // 8 semanas
            {9, 25700, 288000}, // 9 semanas
            {10, 25700, 288000}, // 10 semanas
            {11, 25700, 288000}, // 11 semanas
            {12, 25700, 288000}, // 12 semanas
            {13, 13300, 254000}, // 13 semanas
            {14, 13300, 254000}, // 14 semanas
            {15, 13300, 254000}, // 15 semanas
            {16, 13300, 254000}  // 16 semanas
    };

    /**
     * Calcula a idade gestacional com base no valor de Beta hCG e data da última menstruação
     *
     * @param betaHcg Valor do Beta hCG em mUI/ml
     * @param ultimaMenstruacao Data da última menstruação
     * @return String indicando a interpretação do resultado
     */
    public String calcularIdadeGestacional(double betaHcg, Date ultimaMenstruacao) {
        if (ultimaMenstruacao == null) {
            return "Data da última menstruação não informada";
        }

        if (betaHcg < 0) {
            return "Valor de Beta hCG inválido";
        }

        Date hoje = new Date();
        long diferencaMs = hoje.getTime() - ultimaMenstruacao.getTime();
        long semanasGestacionais = TimeUnit.MILLISECONDS.toDays(diferencaMs) / 7;

        if (betaHcg < 25) {
            if (semanasGestacionais < 4) {
                return "Resultado negativo (<25 mUI/mL): indica que, provavelmente, não há gestação em curso. " +
                        "Como a DUM é recente (menos de 4 semanas), existe possibilidade de falso negativo, " +
                        "sendo recomendado repetir o exame em 1 semana.";
            } else {
                return "Resultado negativo (<25 mUI/mL): indica que, provavelmente, não há gestação em curso.";
            }
        }

        if (semanasGestacionais < 3 || semanasGestacionais > 16) {
            return "Idade gestacional estimada (" + semanasGestacionais + " semanas) está fora do intervalo " +
                    "de interpretação desta calculadora (3-16 semanas). É necessária avaliação médica.";
        }

        double[] faixaAtual = null;
        for (double[] faixa : FAIXAS_BETA_HCG) {
            if (faixa[0] == semanasGestacionais) {
                faixaAtual = faixa;
                break;
            }
        }

        if (faixaAtual == null) {
            return "Não foi possível determinar a faixa de referência para " + semanasGestacionais + " semanas.";
        }

        double valorMinimo = faixaAtual[1];
        double valorMaximo = faixaAtual[2];

        if (betaHcg >= valorMinimo && betaHcg <= valorMaximo) {
            return "Resultado positivo compatível com a idade gestacional: o valor de hCG (" +
                    String.format("%.1f", betaHcg) + " mUI/mL) está dentro do intervalo esperado para " +
                    semanasGestacionais + " semanas de gestação (" +
                    String.format("%.0f", valorMinimo) + "-" + String.format("%.0f", valorMaximo) + " mUI/mL).";
        } else if (betaHcg < valorMinimo) {
            return "Resultado positivo abaixo do esperado: o valor de hCG (" +
                    String.format("%.1f", betaHcg) + " mUI/mL) está abaixo do intervalo esperado para " +
                    semanasGestacionais + " semanas (" + String.format("%.0f", valorMinimo) + "-" +
                    String.format("%.0f", valorMaximo) + " mUI/mL). Pode indicar problemas na evolução da " +
                    "gravidez ou datação incorreta; é necessário acompanhamento médico.";
        } else {
            return "Resultado positivo acima do esperado: o valor de hCG (" +
                    String.format("%.1f", betaHcg) + " mUI/mL) está acima do intervalo esperado para " +
                    semanasGestacionais + " semanas (" + String.format("%.0f", valorMinimo) + "-" +
                    String.format("%.0f", valorMaximo) + " mUI/mL). Pode sugerir uma gestação gemelar ou " +
                    "erro de datação; também deve ser avaliado por um profissional de saúde.";
        }
    }
}