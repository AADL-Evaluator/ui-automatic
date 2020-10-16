package org.osate.aadl.evaluator.ui.automaticWizard.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.osate.aadl.evaluator.automatic.AutomaticBinding;

public class EditActionListener implements ActionListener
{
    private final AutomaticBindingListJPanel panel;
    
    public EditActionListener( AutomaticBindingListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        if( panel.getTreeTable().getSelectedRow() == -1 )
        {
            return ;
        }
        
        for( int row : panel.getTreeTable().getSelectedRows() )
        {
            TreePath path = panel.getTreeTable().getPathForRow( row );
            open( (DefaultMutableTreeTableNode) path.getLastPathComponent() );
        }
    }
    
    private void open( DefaultMutableTreeTableNode node )
    {
        if( !(node.getUserObject() instanceof AutomaticBinding) )
        {
            return ;
        }
        
        final AutomaticBindingJDialog dialog = new AutomaticBindingJDialog( 
            SwingUtilities.getWindowAncestor( panel ) 
        );
        
        dialog.setBinding( (AutomaticBinding) node.getUserObject() );
        dialog.setVisible( true );
        dialog.dispose();
        
        if( dialog.isSaved() )
        {
            panel.getTreeTableModel().setUserObject( 
                node , 
                dialog.getBinding() 
            );
        }
    }
    
}