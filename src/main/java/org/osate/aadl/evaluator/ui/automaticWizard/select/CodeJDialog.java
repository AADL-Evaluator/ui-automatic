package org.osate.aadl.evaluator.ui.automaticWizard.select;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jdesktop.swingx.JXHeader;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.automatic.AutomaticOption;
import org.osate.aadl.evaluator.automatic.AutomaticOptions;
import org.osate.aadl.evaluator.project.Subcomponent;
import org.osate.aadl.evaluator.reqspec.SystemRequirement;

public class CodeJDialog extends javax.swing.JDialog 
{
    private final Set<String> added;
    
    public CodeJDialog( java.awt.Window parent )
    {
        super( parent );
        
        added = new HashSet<>();
        
        initComponents();
        init();
        
        setTitle( "Component View" );
        setSize( 800 , 500 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        add( new JXHeader( 
            "Component View" , 
            "It is code of the component selected."
        ) , BorderLayout.NORTH );
        
        aadlJTextArea.setFont( new Font( Font.MONOSPACED , Font.PLAIN , 13 ) );
    }
    
    public void setSubcomponent( Subcomponent subcomponent )
    {
        setComponent( subcomponent.getComponent() );
        append( subcomponent.getRequirements() );
    }
    
    public void setComponent( Component component )
    {
        StringBuilder builder = new StringBuilder();
        addLine( builder , component );
        aadlJTextArea.setText( builder.toString() );
        aadlJTextArea.setCaretPosition( 0 );
    }
    
    public void setOptions( AutomaticOptions options )
    {
        StringBuilder builder = new StringBuilder();
        addLine( builder , options.getDeclaration().getComponent() );
        
        for( AutomaticOption option : options.getComponents() )
        {
            addLine( builder , option.getComponent() );
        }
        
        aadlJTextArea.setText( builder.toString() );
        aadlJTextArea.setCaretPosition( 0 );
        
        if( options.getDeclaration() instanceof Subcomponent )
        {
            append( ((Subcomponent) options.getDeclaration()).getRequirements() );
        }
   }
    
    private void addLine( StringBuilder builder , Component c )
    {
        if( c == null 
            || added.contains( c.getFullName() ) )
        {
            return ;
        }
        
        added.add( c.getFullName() );
        
        addLine( builder , c.getComponentExtended() );
        addLine( builder , c.getComponentImplemented() );
        
        append( builder , c );
    }
    
    private void append( StringBuilder builder , Component c )
    {
        if( !builder.toString().isEmpty() )
        {
            builder.append( "\n" );
        }
        
        // first line
        builder.append( c.getType() );
        builder.append( c.isGroup() ? " implementation " : " " );
        builder.append( c.isImplementation() ? " implementation " : " " );
        builder.append( c.getName().trim() );
        builder.append( c.getExtend() == null || c.getExtend().isEmpty() 
            ? "" 
            : " extend " + c.getExtend()
        );
        
        append( builder , "features"      , c.getFeatures().values()      , ":" );
        append( builder , "subcomponents" , c.getSubcomponents().values() , ":" );
        append( builder , "connections"   , c.getConnections().values()   , ":" );
        append( builder , "properties"    , c.getProperties()             , "=>" );
        
        // last line
        builder.append( "\nend " );
        builder.append( c.getName() );
        builder.append( ";\n" );
    }
    
    private void append( StringBuilder builder , String name , Collection<? extends Declaration> declarations , String connector )
    {
        if( declarations.isEmpty() )
        {
            return ;
        }
        
        builder.append( "\n  " );
        builder.append( name );
        //builder.append( "\n" );
                
        for( Declaration d : declarations )
        {
            builder.append( "\n    " );
            builder.append( d.getName() );
            builder.append( " " );
            builder.append( connector );
            builder.append( " " );
            builder.append( d.getValue() );
            builder.append( ";" );
        }
    }
    
    public void append( List<SystemRequirement> requirements )
    {
        StringBuilder builder = new StringBuilder();
        
        for( SystemRequirement req : requirements )
        {
            builder.append( "  requirement " );
            builder.append( req.getName() );
            builder.append( " : \"" );
            builder.append( req.getTitle() );
            builder.append( "\"" );
            
            builder.append( "\n  for " );
            builder.append( req.getTarget() );
            
            builder.append( "\n  [\n" );
            
            reqAppend( builder , "category"        , req.getCategory()        , "\n" );
            reqAppend( builder , "change uncertainty" , req.getChangeUncertainty() , "\n" );
            reqAppend( builder , "decomposes"      , req.getDecomposes()      , "\n" );
            reqAppend( builder , "development stakerholder" , req.getDevelopmentStakeholder() , "\n" );
            reqAppend( builder , "dropper"         , req.getDropped()         , "\n" );
            reqAppend( builder , "evolves"         , req.getEvolves()         , "\n" );
            reqAppend( builder , "inherits"        , req.getInherits()        , "\n" );
            reqAppend( builder , "mitigates"       , req.getMitigates()       , "\n" );
            reqAppend( builder , "value predicate" , req.getPredicate()       , "\n" );
            reqAppend( builder , "Rationale"       , req.getRationale()       , "\n" );
            reqAppend( builder , "refines"         , req.getRefines()         , "\n" );
            reqAppend( builder , "see document"    , req.getSeeDocuments()    , "\n" );
            reqAppend( builder , "see goal"        , req.getSeeGoals()        , "\n" );
            reqAppend( builder , "see requirement" , req.getSeeRequirements() , "\n" );
            reqAppend( builder , "when condition"  , req.getWhenCondition()   , "\n" );
            
            reqAppend( builder , "compute"  , req.getComputeds() , "\n" );
            reqAppend( builder , "constant" , req.getConstants() , "\n" );
            
            builder.append( "  ]\n\n" );
        }
        
        reqspecJTextArea.setText( builder.toString() );
        reqspecJTextArea.setCaretPosition( 0 );
    }
    
    private void reqAppend( StringBuilder builder , String name , Collection values , String end )
    {
        if( values == null || values.isEmpty() )
        {
            return ;
        }
        
        for( Object value : values )
        {
            reqAppend( builder , name , value.toString() , end );
        }
    }
    
    private void reqAppend( StringBuilder builder , String name , String value , String end )
    {
        if( value == null || value.trim().isEmpty() )
        {
            return ;
        }
        
        builder.append( "    " );
        builder.append( name )
            .append( " " )
            .append( value )
            .append( end );
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        aadlJTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        reqspecJTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        aadlJTextArea.setEditable(false);
        aadlJTextArea.setColumns(20);
        aadlJTextArea.setRows(5);
        jScrollPane1.setViewportView(aadlJTextArea);

        jTabbedPane1.addTab("AADL", jScrollPane1);

        reqspecJTextArea.setEditable(false);
        reqspecJTextArea.setColumns(20);
        reqspecJTextArea.setRows(5);
        jScrollPane2.setViewportView(reqspecJTextArea);

        jTabbedPane1.addTab("Reqspec", jScrollPane2);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea aadlJTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea reqspecJTextArea;
    // End of variables declaration//GEN-END:variables
}
