package com.company;

import com.company.codegen.CodeBlock;
import com.company.errors.SymbolErrorsContainer;
import com.company.errors.TableErrorsContainer;
import com.company.intermedary.ThreeAddressCode;
import com.company.tables.TypesTable;
import com.company.visitor.YAPLSymbolsVisitor;
import com.company.yapl.YAPLLexer;
import com.company.yapl.YAPLParser;
import com.company.visitor.YAPLTypeVisitor;
import com.company.yapl.YAPLVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

//TODO LIST: Add casting

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
//        launch(args);
	    // Write your code here
        StringBuilder content = new StringBuilder();
        try {
//            File file = new File("./src/com/company/samples/ackerman.txt");
//            File file = new File("./src/com/company/samples/error_easy.txt");
//            File file = new File("./src/com/company/samples/cool.cl");
//            File file = new File("./src/com/company/samples/arith.cl");
//            File file = new File("./src/com/company/samples/primes.cl");
//            File file = new File("./src/com/company/samples/recur.cl");
            File file = new File("./src/com/company/samples/list.cl");
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
        String intermediateCode = TAC.printTAC();
        System.out.println(intermediateCode);
        createFile("intermediate_code.txt", intermediateCode);
        List<CodeBlock> code = TAC.generateCode();
        StringBuilder codeBuilder = new StringBuilder();
        System.out.println("\n\n\n\nFELICIDADES TODO ESTA BIEN\n\n\n\n");
        for (CodeBlock c:
             code) {
            codeBuilder
                    .append("\t".repeat(Math.max(0, c.getTab())));
            codeBuilder
                    .append(c.getBlock()).append("\n");
        }
        createFile("target_code.txt", codeBuilder.toString());
        String template = readTemplate();
        createFile("final_code.asm", template.replace("###TEMPLATE###", codeBuilder.toString()));
        System.out.println("\n\n\n\nFELICIDADES TODO ESTA BIEN");
    }

    public static void createFile(String fileName, String content) {
        try {
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
//                System.out.println("File created: " + myObj.getName());
            } else {
//                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readTemplate() throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        File file = new File("./TEMPLATE.asm");
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            content.append(scanner.nextLine()).append("\n");
        }
        return content.toString();
    }
}
