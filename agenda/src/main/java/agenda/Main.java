package agenda;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

import io.IO;


public class Main {
	
	private static String FICHERO = "agenda.csv";

	public static void main(String[] args) {
		RandomAccessFile raf = null;
		boolean dentro = true;
		
		try {
			raf = new RandomAccessFile(FICHERO, "rw");
			try {
				if(raf.length() == 0) {
			String cabecera = "usuario,nombre,telefono,edad\n";;
					raf.seek(raf.length());
			raf.writeBytes(cabecera);
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner sc = new Scanner(System.in);
		
		while(dentro) {
			 System.out.println("Seleccione una opción:");
	         System.out.println("1. Buscar por código de usuario");
	         System.out.println("2. Buscar por nombre");
	         System.out.println("3. Mostrar la agenda completa");
	         System.out.println("4. Añadir un contacto");
	         System.out.println("5. Borrar un contacto");
	         System.out.println("6. Salir");
	         
	         int selec = IO.readInt();
	         try {
	             raf.seek(29);
	             }catch (Exception e) {
	            	 e.printStackTrace();
	             }
	         
	         switch(selec) {
	         case 1:
	        	 buscarUsuario(raf,sc);
	        	 break;
	         case 2:
	        	 buscarNombre(raf,sc);
	        	 break;
	         case 3:
	        	 mostrarAgenda(raf);
	        	 break;
	         case 4: 
	        	 Persona persona = leerDatos(sc);
	 			if (persona == null) {
	 				sc.close();
	 				try {
	 					raf.close();
	 				} catch (IOException e) {
	 					e.printStackTrace();
	 				}			
	 				return;
	 			}
	 			grabarEnCSV(raf, persona);
	 			break;
	         case 5:
	        	 borrarUsuario(raf,sc);
	        	 break;
	         case 6:
	        	 sc.close();
	        	 dentro = false;
                 try {
                     raf.close();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
	         default:
	        	 System.out.println("Opcion no disponible");
	        	 break;
	         
	         }
		}
	         
		}
		

	private static void buscarUsuario(RandomAccessFile raf, Scanner sc) {
		try {
			boolean encontrado= false;
			System.out.println("usuario ?");
			String busqueda=IO.readString();
			while(raf.getFilePointer()<raf.length()) {
				String linea=raf.readLine();
				String[] campos= linea.split(",");
				
				
				if(campos[0].equals(busqueda)&& !campos[0].contains("borrado")) {
					System.out.println(linea);
					encontrado=true;
				} 
			}
			if(!encontrado)
				System.out.println("Usuario no encontrado");
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void borrarUsuario(RandomAccessFile raf, Scanner sc) {
	    try {
	        boolean encontrado = false;
	        System.out.println("Usuario a borrar ?");
	        String busqueda = IO.readString();
	        long posicionInicial = raf.getFilePointer();

	        while (raf.getFilePointer() < raf.length()) {
	            long posicionActual = raf.getFilePointer();
	            String linea = raf.readLine();
	            String[] campos = linea.split(",");

	            if (campos[0].equals(busqueda)&& !campos[0].contains("borrado")) {
	                raf.seek(posicionActual);

	                String registroActualizado = "borrado ";
	                raf.writeBytes(registroActualizado);

	                raf.seek(posicionInicial);

	                System.out.println("Usuario borrado");
	                encontrado = true;
	                break;
	            }
	        }
 
	        if (!encontrado)
	            System.out.println("Usuario no encontrado");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	private static void buscarNombre(RandomAccessFile raf, Scanner sc) {
		try {
			boolean encontrado= false;
			System.out.println("nombre ?");
			String busqueda=IO.readString();
			while(raf.getFilePointer()<raf.length()) {
				String linea=raf.readLine();
				String[] campos= linea.split(",");
				
				
				if(campos[1].contains(busqueda)&& !campos[0].contains("borrado")) {
					System.out.println(linea);
					encontrado=true;
				} 
			}
			if(!encontrado)
				System.out.println("Usuario no encontrado");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void grabarEnCSV(RandomAccessFile raf, Persona persona) {
		try {
			String registro = String.format("%s,%s,%s,%d\n",
					persona.getUsuario().toString(),
					persona.getNombre(),
					persona.getTelefono(),
					persona.getEdad());
					raf.seek(raf.length());
			raf.writeBytes(registro);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private static Persona leerDatos(Scanner sc) {
			System.out.println("Nombre ? ");
			String nombre = IO.readString();
			if (nombre.isBlank()) {
				sc.close();
				return null;
			}
			
			System.out.println("Teléfono ? ");
			String telefono = IO.readString();
			
			System.out.println("Edad ? ");
			int edad = IO.readInt();

			

			return new Persona(nombre, telefono, edad);
		}
	
	private static void mostrarAgenda(RandomAccessFile raf) {
        try {
        	raf.seek(0);
            while(raf.getFilePointer()<raf.length()) {
            String linea=raf.readLine();
            System.out.println(linea);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}