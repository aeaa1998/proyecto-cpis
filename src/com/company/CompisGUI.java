package com.company;

import com.company.codegen.CodeBlock;
import com.company.errors.SymbolErrorsContainer;
import com.company.errors.TableErrorsContainer;
import com.company.errors.YAPLError;
import com.company.intermedary.ThreeAddressCode;
import com.company.tables.SymbolTable;
import com.company.tables.TypesTable;
import com.company.visitor.YAPLSymbolsVisitor;
import com.company.visitor.YAPLTypeVisitor;
import com.company.yapl.YAPLLexer;
import com.company.yapl.YAPLParser;
import com.company.yapl.YAPLVisitor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.antlr.v4.runtime.ANTLRErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.collection.ListModification;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompisGUI extends Application {

    private static final String[] KEYWORDS = new String[] {
            "class", "let", "if", "else", "then", "fi",
            "while", "inherits", "pool", "loop", "new",
            "not", "true", "false", "isvoid", "self",
            "SELF_TYPE"
    };
    private static final String KEYWORD_PATTERN = "\\b(?i)(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String COMMENT_PATTERN = "\\(\\*(\n|.)*?\\*\\)";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String BRACE_PATTERN = "\\{|\\}";



    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
//                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

/*
 */

    public static void main(String[] args) {
        launch(args);
    }

    private void setErrorColor(TextArea console){
        console.setStyle("-fx-text-fill: red");
    }

    @Override
    public void start(Stage primaryStage) {
        int width = 1520;
        int height = 820;

        Button btn = new Button();
        CodeArea codeArea = new CodeArea();
        // add line numbers to the left of area
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setContextMenu(new DefaultContextMenu());

        codeArea.getVisibleParagraphs().addModificationObserver
                (
                        new VisibleParagraphStyler<>(codeArea, this::computeHighlighting)
                );

        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        codeArea.addEventHandler( KeyEvent.KEY_PRESSED, KE ->
        {
            if ( KE.getCode() == KeyCode.ENTER ) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher( codeArea.getParagraph( currentParagraph-1 ).getSegments().get( 0 ) );
                if ( m0.find() ) Platform.runLater( () -> codeArea.insertText( caretPosition, m0.group() ) );
            }
        });


        TextArea console = new TextArea();
        console.setWrapText(true);


        btn.setText("Compilar");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //Clear the text
                console.clear();
                //Clear tables
                TypesTable.getInstance().clear();
                //Clear errors
                TableErrorsContainer.getInstance().resetErrors();
                SymbolErrorsContainer.getInstance().resetErrors();
                //Now set the content and process the compilation
                String content  = codeArea.getText();
                YAPLLexer lexer = new YAPLLexer(CharStreams.fromString(content));
                YAPLParser parser = new YAPLParser(new CommonTokenStream(lexer));
                ParseTree program = parser.program();
                int numberOfSyntaxErrors = parser.getNumberOfSyntaxErrors();
                if (numberOfSyntaxErrors > 0){
                    console.setText("Se encontro " + numberOfSyntaxErrors + " errores sintacticos");
                    setErrorColor(console);
                    return;
                }
                YAPLVisitor<Object> visitor = new YAPLTypeVisitor<>();
                visitor.visit(program);
                TypesTable.getInstance().buildTables();
                if(!TableErrorsContainer.getInstance().getErrors().isEmpty()){
                    TableErrorsContainer.getInstance().printStackTrace();
                    StringBuilder errorText = new StringBuilder("");
                    for(YAPLError error: TableErrorsContainer.getInstance().getErrors()){
                        errorText.append(error.getErrorText()).append("\n");
                    }
                    console.setText(errorText.toString());
                    setErrorColor(console);
                    return;
                }
                YAPLSymbolsVisitor symVisitor = new YAPLSymbolsVisitor();
                symVisitor.visit(program);
                SymbolErrorsContainer symErrors = SymbolErrorsContainer.getInstance();
                if (!symErrors.getErrors().isEmpty()){
                    StringBuilder errorText = new StringBuilder("");
                    for(YAPLError error: symErrors.getErrors()){
                        errorText.append(error.getErrorText()).append("\n");
                    }
                    console.setText(errorText.toString());
                    setErrorColor(console);
                    return;
                }
                ThreeAddressCode tac = symVisitor.getTAC();
                List<CodeBlock> code = tac.generateCode();
                StringBuilder codeBuilder = new StringBuilder();
                for (CodeBlock c: code) {
                    codeBuilder
                            .append("\t".repeat(Math.max(0, c.getTab())));
                    codeBuilder
                            .append(c.getBlock()).append("\n");
                }
                String template = null;
                try {
                    template = readTemplate();
                    createFile("final_code.asm", template.replace("###TEMPLATE###", codeBuilder.toString()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Runtime rt = Runtime.getRuntime();
                try {
                    Process pr = rt.exec("java -jar Mars4_5.jar final_code.asm");
                    try {
                        pr.waitFor();
                        InputStream inputStream = pr.getInputStream();
                        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                        String result = s.hasNext() ? s.next() : "";
                        console.setText(result);
                        console.setStyle("-fx-text-fill: green");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        console.setText("Felicidades se compilo todo bien");
                        console.setStyle("-fx-text-fill: green");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    console.setText("Felicidades se compilo todo bien");
                    console.setStyle("-fx-text-fill: green");
                }

            }
        });

        AnchorPane pane = new AnchorPane();

        ColumnConstraints rowC = new ColumnConstraints();

        codeArea.setWrapText(true);
        //Text area constraints
        AnchorPane.setLeftAnchor(codeArea, 0.0);
        AnchorPane.setRightAnchor(codeArea, 0.0);
        AnchorPane.setTopAnchor(codeArea, 0.0);
        //Buttons anchors
        AnchorPane.setRightAnchor(btn, 0.0);
        AnchorPane.setBottomAnchor(btn, 0.0);
        //Console text
        AnchorPane.setLeftAnchor(console, 0.0);
        AnchorPane.setBottomAnchor(console, 0.0);

        //Text area height
        double codeHeight = 600.0;
        codeArea.setMinHeight(codeHeight);
        codeArea.setMaxHeight(codeHeight);

        //Console area height
        console.setMinHeight(height - codeHeight);
        console.setMaxHeight(height - codeHeight);
        console.setPrefWidth(width * 0.75);
        console.setEditable(false);

        pane.getChildren().add(codeArea);
        pane.getChildren().add(btn);
        pane.getChildren().add(console);


        //

        Scene scene = new Scene(pane, width, height);
        scene.getStylesheets().add(CompisGUI.class.getResource("resources/java-keywords.css").toExternalForm());

        primaryStage.setTitle("Mi compilador");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder;
        spansBuilder = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
//                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private static class VisibleParagraphStyler<PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>>
    {
        private final GenericStyledArea<PS, SEG, S> area;
        private final Function<String, StyleSpans<S>> computeStyles;
        private int prevParagraph, prevTextLength;

        public VisibleParagraphStyler( GenericStyledArea<PS, SEG, S> area, Function<String,StyleSpans<S>> computeStyles )
        {
            this.computeStyles = computeStyles;
            this.area = area;
        }

        @Override
        public void accept( ListModification<? extends Paragraph<PS, SEG, S>> lm )
        {
            if ( lm.getAddedSize() > 0 )
            {
                int paragraph = Math.min( area.firstVisibleParToAllParIndex() + lm.getFrom(), area.getParagraphs().size()-1 );
                String text = area.getText( paragraph, 0, paragraph, area.getParagraphLength( paragraph ) );

                if ( paragraph != prevParagraph || text.length() != prevTextLength )
                {
                    int startPos = area.getAbsolutePosition( paragraph, 0 );
                    Platform.runLater( () -> area.setStyleSpans( startPos, computeStyles.apply( text ) ) );
                    prevTextLength = text.length();
                    prevParagraph = paragraph;
                }
            }
        }
    }
    private static class DefaultContextMenu extends ContextMenu
    {
        private MenuItem fold, unfold, print;

        public DefaultContextMenu()
        {
            fold = new MenuItem( "Fold selected text" );
            fold.setOnAction( AE -> { hide(); fold(); } );

            unfold = new MenuItem( "Unfold from cursor" );
            unfold.setOnAction( AE -> { hide(); unfold(); } );

            print = new MenuItem( "Print" );
            print.setOnAction( AE -> { hide(); print(); } );

            getItems().addAll( fold, unfold, print );
        }

        /**
         * Folds multiple lines of selected text, only showing the first line and hiding the rest.
         */
        private void fold() {
            ((CodeArea) getOwnerNode()).foldSelectedParagraphs();
        }

        /**
         * Unfold the CURRENT line/paragraph if it has a fold.
         */
        private void unfold() {
            CodeArea area = (CodeArea) getOwnerNode();
            area.unfoldParagraphs( area.getCurrentParagraph() );
        }

        private void print() {
            System.out.println( ((CodeArea) getOwnerNode()).getText() );
        }
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
