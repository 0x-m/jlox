// Copyright 2022 mohammad
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAST {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal : Object value",
                "Unary : Token operator, Expr right"));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types)
            throws IOException {

        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package jlox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        defineVisitor(writer, baseName, types);
        // The AST classes.
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);

        }
        // The base accept method.
        writer.println(" abstract <R> R accept(Visitor<R> visitor);");
        writer.println("}");
        writer.close();

    }

    private static void defineType(PrintWriter writer, String baseName,
            String className, String fieldList) {
        writer.println(" static class " + className + " extends " + baseName + " {");

        // Constructor
        writer.println("  " + className + "(" + fieldList + ") {");
        String[] fields = fieldList.split(",");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("    this." + name + " = " + name + ";");
        }

        writer.println("  }");

        // Visitor pattern.
        writer.println();
        writer.println("    @override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("    return visitor.visit" +
                className + baseName + "(this);");
        // Fields
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println("    }");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println(" interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("    }");
    }
}
