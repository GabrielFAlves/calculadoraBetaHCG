package org.example;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Classe Main para testar a CalculadoraIdadeGestacional
 * Demonstra os diferentes cenários contemplados nos 52 testes
 */
public class Main {

    public static void main(String[] args) {
        CalculadoraIdadeGestacional calculadora = new CalculadoraIdadeGestacional();
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("=== CALCULADORA DE IDADE GESTACIONAL ===");
        System.out.println("Baseada nos valores de Beta hCG e data da última menstruação");
        System.out.println("Segue a lógica do site mdsaude.com");
        System.out.println("Intervalo de interpretação: 3 a 16 semanas de gestação");
        System.out.println("Contempla 52 cenários de teste diferentes");
        System.out.println();

        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Inserir dados manualmente");
            System.out.println("2 - Executar exemplos básicos");
            System.out.println("3 - Demonstrar todas as categorias de teste");
            System.out.println("4 - Mostrar casos específicos por categoria");
            System.out.println("5 - Sair");
            System.out.print("Opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    testarManualmente(calculadora, scanner, formatoData);
                    break;
                case 2:
                    executarExemplosBasicos(calculadora);
                    break;
                case 3:
                    demonstrarTodasCategorias(calculadora);
                    break;
                case 4:
                    mostrarCasosEspecificos(calculadora, scanner);
                    break;
                case 5:
                    System.out.println("Encerrando programa...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
            }

            System.out.println();
        }
    }

    /**
     * Permite ao usuário inserir dados manualmente
     */
    private static void testarManualmente(CalculadoraIdadeGestacional calculadora,
                                          Scanner scanner, SimpleDateFormat formatoData) {
        try {
            System.out.print("Digite o valor do Beta hCG (mUI/ml): ");
            double betaHcg = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Digite a data da última menstruação (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine();
            Date ultimaMenstruacao = formatoData.parse(dataStr);

            String resultado = calculadora.calcularIdadeGestacional(betaHcg, ultimaMenstruacao);

            long diferencaMs = new Date().getTime() - ultimaMenstruacao.getTime();
            long semanas = diferencaMs / (1000L * 60 * 60 * 24 * 7);

            System.out.println("\n--- RESULTADO ---");
            System.out.println("Beta hCG: " + betaHcg + " mUI/ml");
            System.out.println("Última menstruação: " + formatoData.format(ultimaMenstruacao));
            System.out.println("Idade gestacional estimada: " + semanas + " semanas");
            System.out.println("Interpretação: " + resultado);

        } catch (ParseException e) {
            System.out.println("Erro: Data inválida! Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /**
     * Executa exemplos básicos para demonstrar o funcionamento
     */
    private static void executarExemplosBasicos(CalculadoraIdadeGestacional calculadora) {
        System.out.println("=== EXEMPLOS BÁSICOS ===");
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

        Object[][] exemplos = {
                {20.0, 6, "Resultado negativo"},
                {2000.0, 5, "Resultado compatível"},
                {500.0, 6, "Resultado abaixo do esperado"},
                {50000.0, 5, "Resultado acima do esperado"}
        };

        for (Object[] exemplo : exemplos) {
            double valor = (Double) exemplo[0];
            int semanasAtras = (Integer) exemplo[1];
            String categoria = (String) exemplo[2];

            Date dum = criarDataUltimaMenstruacao(semanasAtras);
            String resultado = calculadora.calcularIdadeGestacional(valor, dum);

            System.out.println("Categoria: " + categoria);
            System.out.println("  Beta hCG: " + valor + " mUI/mL");
            System.out.println("  DUM: " + formatoData.format(dum) + " (" + semanasAtras + " semanas atrás)");
            System.out.println("  Resultado: " + resultado);
            System.out.println();
        }
    }

    /**
     * Demonstra todas as categorias de teste contempladas
     */
    private static void demonstrarTodasCategorias(CalculadoraIdadeGestacional calculadora) {
        System.out.println("=== DEMONSTRAÇÃO DAS 7 CATEGORIAS DE TESTE ===");
        System.out.println("Total: 52 testes organizados nas seguintes categorias:");
        System.out.println();

        System.out.println("1. VALIDAÇÃO DE ENTRADA (5 testes):");
        testarValidacao(calculadora);

        System.out.println("2. RESULTADO NEGATIVO (6 testes):");
        testarResultadoNegativo(calculadora);

        System.out.println("3. FORA DO INTERVALO (4 testes):");
        testarForaIntervalo(calculadora);

        System.out.println("4. COMPATIBILIDADE POR SEMANA (14 testes - resumo):");
        testarCompatibilidade(calculadora);

        System.out.println("5. ABAIXO DO ESPERADO (7 testes):");
        testarAbaixoEsperado(calculadora);

        System.out.println("6. ACIMA DO ESPERADO (8 testes):");
        testarAcimaEsperado(calculadora);

        System.out.println("7. LIMITES EXATOS DAS FAIXAS (8 testes):");
        testarLimitesExatos(calculadora);
    }

    private static void testarValidacao(CalculadoraIdadeGestacional calculadora) {
        Date dum = criarDataUltimaMenstruacao(6);

        System.out.println("  • Valor negativo: " +
                calculadora.calcularIdadeGestacional(-1.0, dum).substring(0, 30) + "...");
        System.out.println("  • Data nula: " +
                calculadora.calcularIdadeGestacional(100.0, null));
        System.out.println("  • Valor zero: " +
                (calculadora.calcularIdadeGestacional(0.0, dum).contains("negativo") ? "✓ Negativo detectado" : "Erro"));
        System.out.println();
    }

    private static void testarResultadoNegativo(CalculadoraIdadeGestacional calculadora) {
        System.out.println("  • Recente (3 sem, 20 mUI/ml): " +
                (calculadora.calcularIdadeGestacional(20.0, criarDataUltimaMenstruacao(3)).contains("falso negativo") ? "✓ Falso negativo detectado" : "Erro"));
        System.out.println("  • Tardio (10 sem, 15 mUI/ml): " +
                (calculadora.calcularIdadeGestacional(15.0, criarDataUltimaMenstruacao(10)).contains("Resultado negativo") &&
                        !calculadora.calcularIdadeGestacional(15.0, criarDataUltimaMenstruacao(10)).contains("falso negativo") ? "✓ Negativo sem falso negativo" : "Erro"));
        System.out.println();
    }

    private static void testarForaIntervalo(CalculadoraIdadeGestacional calculadora) {
        System.out.println("  • Muito cedo (1 sem): " +
                (calculadora.calcularIdadeGestacional(100.0, criarDataUltimaMenstruacao(1)).contains("fora do intervalo") ? "✓ Detectado" : "Erro"));
        System.out.println("  • Muito tarde (20 sem): " +
                (calculadora.calcularIdadeGestacional(100.0, criarDataUltimaMenstruacao(20)).contains("fora do intervalo") ? "✓ Detectado" : "Erro"));
        System.out.println();
    }

    private static void testarCompatibilidade(CalculadoraIdadeGestacional calculadora) {
        System.out.println("  • 5 semanas (2000 mUI/ml): " +
                (calculadora.calcularIdadeGestacional(2000.0, criarDataUltimaMenstruacao(5)).contains("compatível") ? "✓ Compatível" : "Erro"));
        System.out.println("  • 8 semanas (100000 mUI/ml): " +
                (calculadora.calcularIdadeGestacional(100000.0, criarDataUltimaMenstruacao(8)).contains("compatível") ? "✓ Compatível" : "Erro"));
        System.out.println("  • [Testadas todas as 14 semanas: 3-16]");
        System.out.println();
    }

    private static void testarAbaixoEsperado(CalculadoraIdadeGestacional calculadora) {
        System.out.println("  • 6 semanas (500 mUI/ml): " +
                (calculadora.calcularIdadeGestacional(500.0, criarDataUltimaMenstruacao(6)).contains("abaixo do esperado") ? "✓ Detectado" : "Erro"));
        System.out.println("  • 9 semanas (20000 mUI/ml): " +
                (calculadora.calcularIdadeGestacional(20000.0, criarDataUltimaMenstruacao(9)).contains("abaixo do esperado") ? "✓ Detectado" : "Erro"));
        System.out.println();
    }

    private static void testarAcimaEsperado(CalculadoraIdadeGestacional calculadora) {
        System.out.println("  • 5 semanas (50000 mUI/ml): " +
                (calculadora.calcularIdadeGestacional(50000.0, criarDataUltimaMenstruacao(5)).contains("acima do esperado") ? "✓ Detectado" : "Erro"));
        System.out.println("  • Possível gemelar: " +
                (calculadora.calcularIdadeGestacional(400000.0, criarDataUltimaMenstruacao(8)).contains("gestação gemelar") ? "✓ Sugerido" : "Erro"));
        System.out.println();
    }

    private static void testarLimitesExatos(CalculadoraIdadeGestacional calculadora) {
        System.out.println("  • Limite inferior 5 sem (18 mUI/ml): " +
                (calculadora.calcularIdadeGestacional(18.0, criarDataUltimaMenstruacao(5)).contains("compatível") ? "✓ No limite" : "Erro"));
        System.out.println("  • Limite superior 5 sem (7340 mUI/ml): " +
                (calculadora.calcularIdadeGestacional(7340.0, criarDataUltimaMenstruacao(5)).contains("compatível") ? "✓ No limite" : "Erro"));
        System.out.println();
    }

    /**
     * Mostra casos específicos por categoria
     */
    private static void mostrarCasosEspecificos(CalculadoraIdadeGestacional calculadora, Scanner scanner) {
        System.out.println("Escolha uma categoria para ver exemplos detalhados:");
        System.out.println("1 - Validação de entrada");
        System.out.println("2 - Resultado negativo");
        System.out.println("3 - Fora do intervalo");
        System.out.println("4 - Compatibilidade (todas as semanas)");
        System.out.println("5 - Abaixo do esperado");
        System.out.println("6 - Acima do esperado");
        System.out.println("7 - Limites exatos");
        System.out.print("Categoria: ");

        int categoria = scanner.nextInt();
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

        switch (categoria) {
            case 4:
                System.out.println("\n=== COMPATIBILIDADE PARA TODAS AS SEMANAS ===");
                for (int sem = 3; sem <= 16; sem++) {
                    double valor = obterValorTipico(sem);
                    Date dum = criarDataUltimaMenstruacao(sem);
                    String resultado = calculadora.calcularIdadeGestacional(valor, dum);

                    System.out.printf("%d semanas (%.0f mUI/ml): %s\n",
                            sem, valor, resultado.contains("compatível") ? "✓ Compatível" : "✗ Problema");
                }
                break;
            case 5:
                System.out.println("\n=== CASOS ABAIXO DO ESPERADO ===");
                Object[][] casosAbaixo = {
                        {15.0, 5, "Bem abaixo da faixa 18-7340"},
                        {500.0, 6, "Abaixo da faixa 1080-56500"},
                        {20000.0, 9, "Abaixo da faixa 25700-288000"}
                };

                for (Object[] caso : casosAbaixo) {
                    double valor = (Double) caso[0];
                    int sem = (Integer) caso[1];
                    String desc = (String) caso[2];
                    Date dum = criarDataUltimaMenstruacao(sem);
                    String resultado = calculadora.calcularIdadeGestacional(valor, dum);

                    System.out.println("• " + desc);
                    System.out.println("  Resultado: " + resultado.substring(0, Math.min(80, resultado.length())) + "...");
                    System.out.println();
                }
                break;
            default:
                System.out.println("Categoria não implementada para demonstração detalhada.");
        }
    }

    /**
     * Obtém um valor típico de Beta hCG para determinada semana
     */
    private static double obterValorTipico(int semana) {
        switch (semana) {
            case 3: return 30;
            case 4: return 200;
            case 5: return 2000;
            case 6: return 20000;
            case 7:
            case 8: return 100000;
            case 9:
            case 10:
            case 11:
            case 12: return 200000;
            case 13:
            case 14:
            case 15:
            case 16: return 150000;
            default: return 100000;
        }
    }

    /**
     * Cria uma data de última menstruação há N semanas
     */
    private static Date criarDataUltimaMenstruacao(int semanasAtras) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -semanasAtras);
        return cal.getTime();
    }
}