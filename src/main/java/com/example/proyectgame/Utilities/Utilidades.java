package com.example.proyectgame.Utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Utilidades {
    /** Este metodo pide un string al usuario y lo valida para que no esté vacía o haya varios espacios juntos. También establece un máximo 18 caracteres.
     * @param msn es el mensaje que recibe el usuario para indicarle qué tiene que introducir.
     * @return devuelve la string validada
     */
    public static String pideString (String msn){
        Scanner teclado = new Scanner(System.in);
        boolean esValido = false;
        String input;

        do {
            System.out.println(msn);
            input = teclado.nextLine();

            if (input.isEmpty()){ //comprueba si la cadena está vacía
                System.out.println("Error. El texto no puede estar vacío.");
            }else if (input.contains("  ")){ //comprueba si contiene más de un espacio consecutivo
                System.out.println("Error, no se admiten espacios consecutivos.");
            }else{
                esValido = true;
            }
        }while (!esValido);
        return input;
    }

    /** pide un numero entero al usuario y lo valida.
     * @param msn mensaje que leerá por pantalla el usuario. Indica qué tiene que introducir.
     * @return numero entero validado.
     */
    public static int pideEntero (String msn){
        boolean esValido = false;
        int num = 0;
        Scanner teclado = new Scanner (System.in);

        do {
            try {
                System.out.println(msn);
                num = teclado.nextInt();
                esValido = true;
            } catch (Exception e) {
                System.out.println("Error. Caracter no válido, solo se admiten números enteros.");
                teclado.next();
            }
        } while (!esValido);
        return num;
    }

    public static boolean validarCorreo(String correo){
        String regex = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
        return correo.matches(regex);
    }

    public static LocalDate pideFecha(String mensaje) {
        while (true) {
            String fecha = Utilidades.pideString(mensaje);
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                return LocalDate.parse(fecha, formatoFecha);
            } catch (DateTimeParseException e) {
                System.out.println("Formato incorrecto. Usa el formato dd/MM/yyyy.");
            }
        }
    }


}
