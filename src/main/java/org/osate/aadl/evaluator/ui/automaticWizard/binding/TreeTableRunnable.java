package org.osate.aadl.evaluator.ui.automaticWizard.binding;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.osate.aadl.evaluator.evolution.Binding;
import org.osate.aadl.evaluator.evolution.BusUtils;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.Evolution;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.automatic.AutomaticBinding;
import org.osate.aadl.evaluator.automatic.AutomaticOption;
import org.osate.aadl.evaluator.automatic.AutomaticOptions;
import org.osate.aadl.evaluator.automatic.BusAndCpu;
import org.osate.aadl.evaluator.ui.secondWizard.binding.BindingRunnable;

public class TreeTableRunnable implements Runnable
{
    private final AutomaticBindingListJPanel panel;
    
    public TreeTableRunnable( AutomaticBindingListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void run() 
    {
        createAllBinding();
        add();
        
        panel.showTreeTable();
    }
    
    private void createAllBinding()
    {
        for( AutomaticOptions options : panel.getOptions() )
        {
            final Component system = options.getDeclaration().getParent();
            
            print( "Declaration: " + options.getDeclaration() , 45 );
            print( "System: " + system.getName() , 30 );
            System.out.print( "subcomponents: " + system.getSubcomponentsAll().keySet() );
            System.out.println();
            
            for( final AutomaticOption option : options.getComponents() )
            {
                final Evolution evolution = new Evolution( system );
                evolution.getDeclarations().add( options.getDeclaration() );
                
                evolution.getCandidates().add(
                    new Candidate( 
                        option.getComponent() , 
                        options.getFuncionalities()
                    )
                );

                create( evolution , option );
            }
        }
    }
    
    private void create( final Evolution evolution , final AutomaticOption option )
    {
        List<AutomaticBinding> bindings = new LinkedList<>();
        
        List<BindingRunnable.Relationship> relationships = new LinkedList<>();
        
        BindingRunnable creator = new BindingRunnable( this.panel ){
            @Override
            public Component getSystemCopy() {
                return evolution.getSystem();
            }

            @Override
            public Evolution getEvolution() {
                return evolution;
            }

            @Override
            public void setData( List<Binding> list ) {
                option.getBindings().clear();
            }

            @Override
            public void addData( Relationship relationship ) {
                relationships.add( relationship );
                
                bindings.add( new AutomaticBinding( 
                    evolution.getSystem() , 
                    relationship.getBinding() ,
                    getBusesAndCpus( evolution.getSystem() )
                ) );
            }
        };

        creator.run();
        
        // criar todos os bindings
        option.getBindings().add( bindings );
        
        // criar os bindings alternativos
        createAllBindings( evolution , option , relationships );
    }
    
    private void createAllBindings( final Evolution evolution ,  final AutomaticOption option , final List<BindingRunnable.Relationship> relationships )
    {
        for( BindingRunnable.Relationship r : relationships )
        {
            if( r.getAlternatives().isEmpty() )
            {
                continue ;
            }
            
            List<List<AutomaticBinding>> bindinds = new LinkedList<>();
            
            for( BindingRunnable.Relationship c : r.getAlternatives() )
            {
                for( List<AutomaticBinding> originals : option.getBindings() )
                {
                    List<AutomaticBinding> cloned = new LinkedList<>();
                    
                    for( AutomaticBinding original : originals )
                    {
                        if( c.getBinding().getPartA() != null
                            && c.getBinding().getPartA().equalsIgnoreCase( original.getPartA() ) )
                        {
                            cloned.add( new AutomaticBinding( 
                                evolution.getSystem() , 
                                c.getBinding() ,
                                getBusesAndCpus( evolution.getSystem() )
                            ) );
                        }
                        else
                        {
                            cloned.add( original.clone() );
                        }
                    }

                    bindinds.add( cloned );
                }
            }
            
            option.getBindings().addAll( bindinds );
        }
    }
    
    private void add()
    {
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode( "Options" );
        
        // add the component that will be changed
        for( AutomaticOptions options : panel.getOptions() )
        {
            DefaultMutableTreeTableNode n1 = new DefaultMutableTreeTableNode( options );
            
            // add the candidates of this component
            for( AutomaticOption option : options.getComponents() )
            {
                DefaultMutableTreeTableNode n2 = new DefaultMutableTreeTableNode( option );
                int counter = 1;
                
                // add the all binbing of this candidate
                for( List<AutomaticBinding> bindings : option.getBindings() )
                {
                    DefaultMutableTreeTableNode n3 = new DefaultMutableTreeTableNode( "Alternative #"+ (counter++) );
                    
                    for( AutomaticBinding binding : bindings )
                    {
                        n3.add( new DefaultMutableTreeTableNode( binding ) );
                    
                        System.out.print( "  " );
                        print( "binding: " + binding.toString() , 75 );
                        System.out.print( "subcomponents: " + binding.getSystem().getSubcomponentsAll().keySet() );
                        System.out.println();
                    }
                    
                    n2.add( n3 );
                }
                
                n1.add( n2 );
            }
            
            root.add( n1 );
        }
        
        panel.getTreeTableModel().setRoot( root );
    }
    
    private void print( String text , int size )
    {
        for( int i = 0 ; i < size ; i++ )
        {
            System.out.print( i < text.length() ? text.charAt( i ) : " " );
        }
    }
    
    // TODO: selecionar apenas os barramentos compatíveis?
    private List<BusAndCpu> getBusesAndCpus( Component system )
    {
        List<BusAndCpu> results = new LinkedList<>();
        
        // TODO: gambiarra para ser retirada depois quando decidir quando selecionar os barramentos corretamente.
        if( results.isEmpty() )
        {
            return results;
        }
        
        for( Map.Entry<String,List<String>> entry : BusUtils.getProcessors( system ).entrySet() )
        {
            String bus = entry.getKey();
            
            for( String cpu : entry.getValue() )
            {
                results.add( new BusAndCpu( bus , cpu ) );
            }
        }
        
        return results;
    }
    
}