package co.insou.obfuscator.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class SymbolRenamer implements ClassTransformer {

    private Set<String> usedFields = new HashSet<>();
    private Set<String> usedMethods = new HashSet<>();

    @Override
    public ClassNode transform(ClassNode node)
    {
        this.underscorifyMethodNames(node);
        this.underscorifyFieldNames(node);
        this.underscorifyMethodParams(node);
        this.underscorifyMethodVars(node);

        return node;
    }

    private void underscorifyMethodNames(ClassNode node)
    {
        Map<String, String> renames = new HashMap<>();

        node.methods.stream()
                .filter(method -> shouldObfuscateMethod(method, node))
                .forEach(method ->
                {
                    String badName = badMethodName();

                    renames.put(method.name + method.desc, badName);
                    method.name = badName;
                });

        node.methods.stream()
                .flatMap(this::streamInstructions)
                .filter(insn -> insn instanceof MethodInsnNode)
                .map(insn -> (MethodInsnNode) insn)
                .filter(insn -> insn.owner.equals(node.name))
                .forEach(insn ->
                        insn.name = renames.getOrDefault(insn.name + insn.desc, insn.name));
    }

    private void underscorifyFieldNames(ClassNode node)
    {
        Map<String, String> renames = new HashMap<>();

        node.fields
                .forEach(field ->
                {
                    String badName = badFieldName();

                    renames.put(field.name, badName);
                    field.name = badName;
                });

        node.methods.stream()
                .flatMap(this::streamInstructions)
                .filter(insn -> insn instanceof FieldInsnNode)
                .map(insn -> (FieldInsnNode) insn)
                .filter(insn -> insn.owner.equals(node.name))
                .forEach(insn ->
                        insn.name = renames.getOrDefault(insn.name, insn.name));
    }

    private void underscorifyMethodParams(ClassNode node)
    {
        node.methods.stream()
                .flatMap(this::streamParameters)
                .forEach(this::underscorify);
    }

    private void underscorifyMethodVars(ClassNode node)
    {
        node.methods.stream()
                .flatMap(this::streamVariables)
                .forEach(this::underscorify);
    }

    private boolean shouldObfuscateMethod(MethodNode method, ClassNode clazz)
    {
        boolean isMain = method.name.equals("main") && method.desc.equals("([Ljava/lang/String;)V") && isStatic(method);
        boolean isOverridden = this.isSuperMethod(method, clazz);
        boolean isInit = method.name.equals("<init>");

        return !isMain && !isOverridden && !isInit;
    }

    private Stream<ParameterNode> streamParameters(MethodNode node)
    {
        if (node.parameters != null)
        {
            return node.parameters.stream();
        }

        return Stream.of();
    }

    private Stream<LocalVariableNode> streamVariables(MethodNode node)
    {
        if (node.localVariables != null)
        {
            return node.localVariables.stream()
                    .filter(var -> !var.name.equals("this"));
        }

        return Stream.of();
    }

    private Stream<AbstractInsnNode> streamInstructions(MethodNode node)
    {
        if (node.instructions != null)
        {
            return Stream.of(node.instructions.toArray());
        }

        return Stream.of();
    }

    private void underscorify(ParameterNode node)
    {
        node.name = "_";
    }

    private void underscorify(LocalVariableNode node)
    {
        node.name = "_";
    }

    private String badName()
    {
        StringBuilder nameBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++)
        {
            nameBuilder.append(new char[]{'\u200b', '\u0000'}[ThreadLocalRandom.current().nextInt(2)]);
        }

        return nameBuilder.toString();
    }

    private String badFieldName()
    {
        String name;

        do {
            name = badName();
        } while (this.usedFields.contains(name));

        this.usedFields.add(name);
        return name;
    }

    private String badMethodName()
    {
        String name;

        do {
            name = badName();
        } while (this.usedMethods.contains(name));

        this.usedMethods.add(name);
        return name;
    }

    private boolean isSuperMethod(MethodNode method, ClassNode node)
    {
        if (node.superName == null)
            return false;

        boolean exists = false;
        ClassNode cn;

        try
        {
            ClassReader cr = new ClassReader(node.superName);
            cn = new ClassNode();
            cr.accept(cn, 0);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        for (MethodNode superMethod : cn.methods)
        {
            boolean sameSignature = (superMethod.desc == null && method.desc == null) || (
                    method.desc != null && superMethod.desc != null &&
                    method.desc.equals(superMethod.desc));

            if (superMethod.name.equals(method.name) && sameSignature)
            {
                exists = true;
                break;
            }
        }

        return exists || isSuperMethod(method, cn);
    }

    private boolean isStatic(MethodNode method)
    {
        return (method.access & 0x8) == 0x8;
    }

}
