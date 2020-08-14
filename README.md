# DJF - Desktop Java Forms

Djf is Desktop Java Forms, a compact master-detail UI library like FoxBase, but based on Swing.
Djf uses Hibernate mapping concepts for data, for component layout - Miglayout and RSyntaxTextArea for text panels.

You can see almost all examples of forms definition, data bindings and assistances in [demo application](https://github.com/smart-flex/Djf/releases/download/1.1/djf-demo-1.1-standalone.jar).
After downloading you can run this demo: java -jar djf-demo-1.1-standalone.jar

![One of the Djf demo form](djf-demo.png)

## Main features

* Easy CRUD operations.
* View relationships between parent and multiple types of children. There can be various combinations, for example:
  + master table and its detail table(s);
  + master table and its nested detail table(s);
  + master table and its several detail fields with table(s);
  + several master tables and their detail table(s);
* Reusing components, such as forms, panels and beans.
* Each form has zero to n models and one control panel with following buttons: add record, refresh form, delete record, save, exit form.
* Each form invokes another form (there is no limit).
* Moving between UI-items without mouse (like in DOS forms) by using tab button, up-down arrows (with CTRL combination for text panels)
* Adaptive column display in the grid depending on the resolution.
* Shows wait panel during execution of long operations.
* Supports the following widgets: table, tgrid (tree based on table), combobox and parent-child (linked) combobox, label, text, int, long, num, short, byte, date, textarea, checkbox, period, password, file, phone.
* Supports user activity log.

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

#### Licensing

Djf is issued on under the GNU Lesser General Public License.

#### Support

If you have any issues or questions or suggestions you can send me a letter by email: <gali.shaimardanov@gmail.com>

