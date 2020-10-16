package org.osate.aadl.evaluator.ui.automaticWizard.binding;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JTextArea;
import org.jdesktop.swingx.JXHeader;

public class DetailsJDialog extends javax.swing.JDialog 
{
    
    public DetailsJDialog( java.awt.Window parent )
    {
        super( parent );
        
        initComponents();
        init();
        
        setTitle( "Binding Details" );
        setSize( 600 , 400 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        add( new JXHeader( 
            "Binding Details" , 
            "This window show a resume of the interfaces & connections selected." 
        ) , BorderLayout.NORTH );
        
        viewJTextArea.setFont( new Font( Font.MONOSPACED , Font.PLAIN , 12 ) );
    }

    public JTextArea getViewJTextArea() 
    {
        return viewJTextArea;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        viewJTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        viewJTextArea.setEditable(false);
        viewJTextArea.setColumns(20);
        viewJTextArea.setRows(5);
        jScrollPane1.setViewportView(viewJTextArea);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea viewJTextArea;
    // End of variables declaration//GEN-END:variables
}
