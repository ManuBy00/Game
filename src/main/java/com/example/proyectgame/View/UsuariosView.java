package View;

import com.example.proyectgame.Model.RolUsuario;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Utilities.Utilidades;


public class UsuariosView {
    public static void showMsg(String msg){
        System.out.println(msg);
    }

    public static Usuario pideDatosUsuario() {
        String nombreUsuario = Utilidades.pideString("Introduce el nombre de usuario");
        String correo = pideCorreo();
        String password = pidePassword();
        RolUsuario rol = RolUsuario.JUGADOR;
        Usuario nuevoUsuario = new Usuario(nombreUsuario, correo,password, rol);
        return nuevoUsuario;
    }


    public static String pideCorreo(){
        boolean correoValido = false;
        String correo;
        do {
            correo = Utilidades.pideString("Introduce el correo");
            if (Utilidades.validarCorreo(correo)){
                correoValido = true;
            }else {
                showMsg("El correo introducido no es válido.");
            }
        }while (!correoValido);
        return correo;
    }

    /**
     * Metodo para pedir una contraseña al usuario
     *
     * @return devuelve un String con la contraseña introducida por el usuario
     */
    public static String pidePassword() {
        String password;
        boolean esValido = false;

        do {
            password = Utilidades.pideString("Introduce la contraseña");
            if (password.length() < 8) {
                showMsg("La contraseña debe tener al menos 8 caracteres");
            } else {
                esValido = true;
            }
        } while (!esValido);

        return password;
    }
}
