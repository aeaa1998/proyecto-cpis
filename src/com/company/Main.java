package com.company;

import com.company.errors.SymbolErrorsContainer;
import com.company.errors.TableErrorsContainer;
import com.company.intermedary.ThreeAddressCode;
import com.company.tables.TypesTable;
import com.company.visitor.YAPLSymbolsVisitor;
import com.company.yapl.YAPLLexer;
import com.company.yapl.YAPLParser;
import com.company.visitor.YAPLTypeVisitor;
import com.company.yapl.YAPLVisitor;
import javafx.application.Application;
import javafx.stage.Stage;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.util.Scanner;

//TODO LIST: Add casting

public class Main {

    public static void main(String[] args) {
//        launch(args);
	    // Write your code here
        StringBuilder content = new StringBuilder();
        try {
//            File file = new File("./src/com/company/samples/easy.txt");
//            File file = new File("./src/com/company/samples/error_easy.txt");
            File file = new File("./src/com/company/samples/primes.cl");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                content.append(scanner.nextLine()).append("\n");
            }

        }catch (Exception e){
            content = new StringBuilder("class Main INHERITS IO {\n" +
                    "    main() : SELF_TYPE {\n" +
                    "\t{\n" +
                    "\t    out_string((new Object).type_name().substr(4,1));\n" +
                    "\t    out_string((isvoid self).type_name().substr(1,3));\n" +
                    "\t    out_string(\"\\n\");\n" + "" +
                    "\t}\n" +
                    "    };\n" +
                    "    \n" +
                    "    set_a(a: Int, b: Int) : SELF_TYPE {\n" +
                    "    };\n" +
                    "};\n");
        }



//        System.out.println(content);
//        return;
//        String content = "id: Int;";
        YAPLLexer lexer = new YAPLLexer(CharStreams.fromString(content.toString()));
        YAPLParser parser = new YAPLParser(new CommonTokenStream(lexer));
        ParseTree program = parser.program();
        if (parser.getNumberOfSyntaxErrors() > 0){
            return;
        }
        YAPLVisitor<Object> visitor = new YAPLTypeVisitor<>();
        visitor.visit(program);
        TypesTable.getInstance().buildTables();
        if(!TableErrorsContainer.getInstance().getErrors().isEmpty()){
            TableErrorsContainer.getInstance().printStackTrace();
            //Finish printing the type errors
            return;
        }
        YAPLSymbolsVisitor symVisitor = new YAPLSymbolsVisitor();
        symVisitor.visit(program);
        SymbolErrorsContainer symErrors = SymbolErrorsContainer.getInstance();
        if (!symErrors.getErrors().isEmpty()){
            symErrors.printStackTrace();
            return;
        }

        ThreeAddressCode TAC = symVisitor.getTAC();
        TAC.printTAC();
        System.out.println("\n\n\n\nFELICIDADES TODO ESTA BIEN");
    }


}
