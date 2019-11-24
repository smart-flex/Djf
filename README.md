# DJF - Desktop Java Forms

Djf is a desktop java forms, the compact and litle master-detail UI library like as FoxBase, but based on Swing.
Djf uses Hibernate mapping concepts for data, for component layout - Miglayout and RSyntaxTextArea for text panels.

Almost all of them, examples of forms definition and assistances you can see in [demo application](https://github.com/smart-flex/Djf/releases/download/1.0/djf-demo-1.0-standalone.jar).

![One of the Djf demo form](djf-demo.png)

## Main features

This library allows:

* easily do CRUD operations
* view relationships between parent and more than one different kind of children
* move between UI-items without mouse (like in DOS forms), by using tab button, up-down arrows (with CTRL combination for text panels)
* support fulfilment long operations with showing wait panel

## Requirements

Djf requires JDK 1.6 or higher.

## Minimalistic code sample
```java
public class HelloWorldSimple {
    public static void main(String[] args) {
        Djf.getConfigurator().configure(null);
        Djf.runForm("ru/smartflex/djf/demo/xml/HelloWorldSimple.frm.xml", SizeFrameEnum.HALF);
    }
}
```
```xml
<form>
    <description>Hello world simple form</description>
    <layout clazz="java.awt.BorderLayout"/>
    <panel>
        <layout clazz="net.miginfocom.swing.MigLayout">
            <param type="string" value="align 50% 50%"/>
        </layout>
        <items>
            <label text="Djf just said: Hello world !!!" font="Arial:B30" fground="#09ACF2"/>
        </items>
    </panel>
</form>
```
![Hello world form](djf-demo-hw-simple.png)

## Licensing

Djf is issued on under the GNU Lesser General Public License.

