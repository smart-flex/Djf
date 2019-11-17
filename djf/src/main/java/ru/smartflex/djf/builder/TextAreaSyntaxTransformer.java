package ru.smartflex.djf.builder;

import java.util.HashMap;
import java.util.Map;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

class TextAreaSyntaxTransformer {

    private static Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("ActionScript", SyntaxConstants.SYNTAX_STYLE_ACTIONSCRIPT);
        map.put("x86 assembler", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
        map.put("BBCode", SyntaxConstants.SYNTAX_STYLE_BBCODE);
        map.put("C", SyntaxConstants.SYNTAX_STYLE_C);
        map.put("Clojure", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
        map.put("C++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        map.put("C#", SyntaxConstants.SYNTAX_STYLE_CSHARP);
        map.put("CSS", SyntaxConstants.SYNTAX_STYLE_CSS);
        map.put("Delphi-Pascal", SyntaxConstants.SYNTAX_STYLE_DELPHI);
        map.put("DTD files", SyntaxConstants.SYNTAX_STYLE_DTD);
        map.put("Fortran", SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        map.put("Groovy", SyntaxConstants.SYNTAX_STYLE_GROOVY);
        map.put(".htaccess", SyntaxConstants.SYNTAX_STYLE_HTACCESS);
        map.put("HTML", SyntaxConstants.SYNTAX_STYLE_HTML);
        map.put("Java", SyntaxConstants.SYNTAX_STYLE_JAVA);
        map.put("JavaScript", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        map.put("JSON", SyntaxConstants.SYNTAX_STYLE_JSON);
        map.put("JSP", SyntaxConstants.SYNTAX_STYLE_JSP);
        map.put("LaTeX", SyntaxConstants.SYNTAX_STYLE_LATEX);
        map.put("Lisp", SyntaxConstants.SYNTAX_STYLE_LISP);
        map.put("Lua", SyntaxConstants.SYNTAX_STYLE_LUA);
        map.put("makefiles", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
        map.put("MXML", SyntaxConstants.SYNTAX_STYLE_MXML);
        map.put("NSIS", SyntaxConstants.SYNTAX_STYLE_NSIS);
        map.put("Perl", SyntaxConstants.SYNTAX_STYLE_PERL);
        map.put("PHP", SyntaxConstants.SYNTAX_STYLE_PHP);
        map.put("properties file", SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
        map.put("Python", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        map.put("Ruby", SyntaxConstants.SYNTAX_STYLE_RUBY);
        map.put("SAS", SyntaxConstants.SYNTAX_STYLE_SAS);
        map.put("Scala", SyntaxConstants.SYNTAX_STYLE_SCALA);
        map.put("SQL", SyntaxConstants.SYNTAX_STYLE_SQL);
        map.put("Tcl", SyntaxConstants.SYNTAX_STYLE_TCL);
        map.put("UNIX shell", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        map.put("Visual Basic", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);
        map.put("Windows batch", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
        map.put("XML", SyntaxConstants.SYNTAX_STYLE_XML);
    }

    private TextAreaSyntaxTransformer() {
    }

    static String getRSyntaxStyleType(String key) {
        return map.get(key);
    }

}
