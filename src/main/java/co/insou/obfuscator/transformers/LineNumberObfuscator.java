package co.insou.obfuscator.transformers;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;

import java.util.stream.Stream;

public class LineNumberObfuscator implements ClassTransformer {

    @Override
    public ClassNode transform(ClassNode node)
    {
        node.methods.forEach(method ->
        {
            Stream.of(method.instructions.toArray())
                    .filter(this::isLineNumberInstruction)
                    .forEach(method.instructions::remove);
        });

        return node;
    }

    private boolean isLineNumberInstruction(AbstractInsnNode insn)
    {
        return insn instanceof LineNumberNode;
    }

}

