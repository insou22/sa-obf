package co.insou.obfuscator.transformers;

import org.objectweb.asm.tree.ClassNode;

public interface ClassTransformer {

    ClassNode transform(ClassNode node);

}
