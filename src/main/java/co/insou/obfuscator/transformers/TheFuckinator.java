package co.insou.obfuscator.transformers;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

import java.nio.charset.Charset;

public class TheFuckinator implements ClassTransformer {

    // hold onto your nuts kiddos
    @Override
    public ClassNode transform(ClassNode node)
    {
        ClassWriter gimmeTheBytes = new ClassWriter(0);
        node.accept(gimmeTheBytes);

        String bytesString = new String(gimmeTheBytes.toByteArray(), Charset.forName("ISO_8859_1"));

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;

        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, node.name, null, "java/lang/ClassLoader", null);

        // TODO: remove?
        cw.visitSource(node.name + ".java", null);

        {
            fv = cw.visitField(Opcodes.ACC_PRIVATE, "extraClassDefs", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/String;[B>;", null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(Opcodes.ACC_PRIVATE, "<init>", "(Ljava/util/Map;)V", "(Ljava/util/Map<Ljava/lang/String;[B>;)V", null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(11, l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/ClassLoader", "<init>", "()V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(12, l1);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitTypeInsn(Opcodes.NEW, "java/util/HashMap");
            mv.visitInsn(Opcodes.DUP);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/HashMap", "<init>", "(Ljava/util/Map;)V", false);
            mv.visitFieldInsn(Opcodes.PUTFIELD, node.name, "extraClassDefs", "Ljava/util/Map;");
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(13, l2);
            mv.visitInsn(Opcodes.RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", "L" + node.name + ";", null, l0, l3, 0);
            mv.visitLocalVariable("extraClassDefs", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/String;[B>;", l0, l3, 1);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(Opcodes.ACC_PROTECTED, "findClass", "(Ljava/lang/String;)Ljava/lang/Class;", "(Ljava/lang/String;)Ljava/lang/Class<*>;", new String[]{"java/lang/ClassNotFoundException"});
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(17, l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, node.name, "extraClassDefs", "Ljava/util/Map;");
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitTypeInsn(Opcodes.CHECKCAST, "[B");
            mv.visitVarInsn(Opcodes.ASTORE, 2);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(19, l1);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            Label l2 = new Label();
            mv.visitJumpInsn(Opcodes.IFNULL, l2);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(20, l3);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitInsn(Opcodes.ARRAYLENGTH);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, node.name, "defineClass", "(Ljava/lang/String;[BII)Ljava/lang/Class;", false);
            mv.visitInsn(Opcodes.ARETURN);
            mv.visitLabel(l2);
            mv.visitLineNumber(23, l2);
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"[B"}, 0, null);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/ClassLoader", "findClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
            mv.visitInsn(Opcodes.ARETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("this", "L" + node.name + ";", null, l0, l4, 0);
            mv.visitLocalVariable("name", "Ljava/lang/String;", null, l0, l4, 1);
            mv.visitLocalVariable("classBytes", "[B", null, l1, l4, 2);
            mv.visitMaxs(5, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, new String[]{"java/lang/Throwable"});
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(29, l0);
            mv.visitLdcInsn(node.name);
            mv.visitVarInsn(Opcodes.ASTORE, 1);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(30, l1);
            mv.visitLdcInsn(bytesString);
            mv.visitVarInsn(Opcodes.ASTORE, 2);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(32, l2);
            mv.visitTypeInsn(Opcodes.NEW, "java/util/HashMap");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
            mv.visitVarInsn(Opcodes.ASTORE, 3);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(34, l3);
            mv.visitVarInsn(Opcodes.ALOAD, 3);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitLdcInsn("ISO_8859_1");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/nio/charset/Charset", "forName", "(Ljava/lang/String;)Ljava/nio/charset/Charset;", false);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "getBytes", "(Ljava/nio/charset/Charset;)[B", false);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitInsn(Opcodes.POP);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLineNumber(36, l4);
            mv.visitTypeInsn(Opcodes.NEW, node.name);
            mv.visitInsn(Opcodes.DUP);
            mv.visitVarInsn(Opcodes.ALOAD, 3);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, node.name, "<init>", "(Ljava/util/Map;)V", false);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, node.name, "findClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
            mv.visitVarInsn(Opcodes.ASTORE, 4);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLineNumber(38, l5);
            mv.visitVarInsn(Opcodes.ALOAD, 4);
            mv.visitLdcInsn("main");
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Class");
            mv.visitInsn(Opcodes.DUP);
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitLdcInsn(Type.getType("[Ljava/lang/String;"));
            mv.visitInsn(Opcodes.AASTORE);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
            mv.visitVarInsn(Opcodes.ASTORE, 5);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitLineNumber(39, l6);
            mv.visitVarInsn(Opcodes.ALOAD, 5);
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/reflect/Method", "setAccessible", "(Z)V", false);
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitLineNumber(40, l7);
            mv.visitVarInsn(Opcodes.ALOAD, 5);
            mv.visitInsn(Opcodes.ACONST_NULL);
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
            mv.visitInsn(Opcodes.DUP);
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitInsn(Opcodes.AASTORE);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitInsn(Opcodes.POP);
            Label l8 = new Label();
            mv.visitLabel(l8);
            mv.visitLineNumber(41, l8);
            mv.visitInsn(Opcodes.RETURN);
            Label l9 = new Label();
            mv.visitLabel(l9);
            mv.visitLocalVariable("args", "[Ljava/lang/String;", null, l0, l9, 0);
            mv.visitLocalVariable("CLASS_NAME", "Ljava/lang/String;", null, l1, l9, 1);
            mv.visitLocalVariable("CLASS_BYTES", "Ljava/lang/String;", null, l2, l9, 2);
            mv.visitLocalVariable("map", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/String;[B>;", l3, l9, 3);
            mv.visitLocalVariable("clazz", "Ljava/lang/Class;", "Ljava/lang/Class<*>;", l5, l9, 4);
            mv.visitLocalVariable("method", "Ljava/lang/reflect/Method;", null, l6, l9, 5);

            mv.visitMaxs(6, 6);
            mv.visitEnd();
        }
        cw.visitEnd();

        byte[] bytes = cw.toByteArray();

        ClassReader reader = new ClassReader(bytes);
        ClassNode newNode = new ClassNode();

        reader.accept(newNode, 0);

        return newNode;
    }

}
