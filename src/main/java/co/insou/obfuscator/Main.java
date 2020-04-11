package co.insou.obfuscator;

import co.insou.obfuscator.transformers.LineNumberObfuscator;
import co.insou.obfuscator.transformers.RandomGarbageAdder;
import co.insou.obfuscator.transformers.SymbolRenamer;
import co.insou.obfuscator.transformers.TheFuckinator;
import org.apache.commons.cli.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;

public class Main {

    private static final int MAX_FUCK_ITERS = 100;

    public static void main(String[] args) throws Exception
    {
        Options options = new Options();

        Option input = new Option("i", "input", true, "class file to obfuscate");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output class file");
        input.setRequired(true);
        options.addOption(output);

        Option lineNums = new Option("l", "Line number obfuscation");
        lineNums.setRequired(false);
        options.addOption(lineNums);

        Option symbols = new Option("s", "Symbol name obfuscation");
        symbols.setRequired(false);
        options.addOption(symbols);

        Option thisObfuscator = new Option("t", "This name obfuscation");
        thisObfuscator.setRequired(false);
        options.addOption(thisObfuscator);

        Option theFuckinator = new Option("fuck", "Activate The Fuckinator");
        theFuckinator.setRequired(false);
        options.addOption(theFuckinator);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try
        {
            cmd = parser.parse(options, args);
        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
            formatter.printHelp("Obfuscator", options);

            System.exit(1);
            return;
        }

        Path path = Paths.get(cmd.getOptionValue("input"));

        if (!Files.exists(path))
        {
            System.out.println("Error: Class file " + args[0] + " does not exist.");

            System.exit(1);
            return;
        }

        byte[] before = Files.readAllBytes(path);

        ClassReader reader = new ClassReader(before);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        if (opt(cmd, "l"))
        {
            node = new LineNumberObfuscator().transform(node);
            System.out.println("Successfully obfuscated line numbers");
        }

        if (opt(cmd, "s"))
        {
            node = new SymbolRenamer().transform(node);
            System.out.println("Successfully obfuscated symbol names");
        }


        if (opt(cmd, "t"))
        {
            node = new RandomGarbageAdder().transform(node);
            System.out.println("Successfully obfuscated this names");
        }

        if (opt(cmd, "fuck"))
        {
            // now, we bullfuckerinate
            int i;
            for (i = 0; i < MAX_FUCK_ITERS; i++)
            {
                try
                {
                    node = new TheFuckinator().transform(node);
                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();
                    break;
                }

                node = new LineNumberObfuscator().transform(node);
                node = new SymbolRenamer().transform(node);
                node = new RandomGarbageAdder().transform(node);
            }

            System.out.println("Successfully fuckinated " + i + " times");
        }



        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);

        byte[] after = writer.toByteArray();

        Files.write(Paths.get(cmd.getOptionValue("output")), after);

        System.out.println("Size before: " + before.length);
        System.out.println("Size after:  " + after.length);
    }

    private static boolean opt(CommandLine cli, String opt)
    {
        return Stream.of(cli.getOptions()).map(Option::getOpt).anyMatch(name -> name.equals(opt));
    }

    @SafeVarargs
    private static ClassVisitor chainUpVisitors(ClassWriter writer, Function<ClassVisitor, ClassVisitor>... visitors) throws Exception
    {
        ClassVisitor base = writer;

        for (Function<ClassVisitor, ClassVisitor> visitor : visitors)
        {
            base = visitor.apply(base);
        }

        return base;
    }

    private static void printBytes(byte[] bytes)
    {
        System.out.println(String.format("%d bytes:\n", bytes.length));

        int curr = 0;

        for (byte b : bytes) {
            System.out.print(String.format("%02x ", b).toUpperCase());
            if (++curr == 32) {
                System.out.println();
                curr = 0;
            }
        }

        if (curr != 0) System.out.println();
    }

}
