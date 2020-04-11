package co.insou.obfuscator.transformers;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;

public class RandomGarbageAdder implements ClassTransformer {

    @Override
    public ClassNode transform(ClassNode node)
    {
        node.methods.forEach(method ->
        {
            LabelNode first = null, last = null;

            for (AbstractInsnNode insn : method.instructions)
            {
                if (insn instanceof LabelNode)
                {
                    if (first == null)
                    {
                        first = (LabelNode) insn;
                    }

                    last = (LabelNode) insn;
                }
            }

            method.localVariables.add(new LocalVariableNode("_\u0000", "I", null, first, last, 0));
        });

        return node;
    }

}
