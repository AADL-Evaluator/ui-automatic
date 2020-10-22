package org.osate.aadl.evaluator.ui.automaticWizard.create;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.osate.aadl.evaluator.evolution.BindingUtils;
import org.osate.aadl.evaluator.evolution.BusUtils;
import org.osate.aadl.evaluator.evolution.Candidate;
import org.osate.aadl.evaluator.evolution.FuncionalityUtils;
import org.osate.aadl.evaluator.automatic.AutomaticBinding;
import org.osate.aadl.evaluator.automatic.AutomaticEvolution;
import org.osate.aadl.evaluator.automatic.AutomaticOption;
import org.osate.aadl.evaluator.automatic.AutomaticOptions;

public class AutomaticCreateStep1
{
    private final AutomaticOptions options;
    
    public AutomaticCreateStep1( AutomaticOptions options )
    {
        this.options = options;
    }
    
    public List<AutomaticEvolution> createAllEvolutions()
    {
        print();
        
        List<AutomaticEvolution> evolutions = new LinkedList<>();
        
        for( AutomaticEvolution composition : create() )
        {
            verifyFuncionatilies( composition );
            
            AutomaticEvolution evolution = new AutomaticEvolution( 
                options.getDeclaration().getParent() 
            );
            
            evolution.setStatus( composition.getStatus() );
            evolution.getMessages().addAll( composition.getMessages() );
            
            evolution.getDeclarations().add( options.getDeclaration() );
            evolution.getComponents().putAll( composition.getComponents() );
            evolution.getCandidates().addAll( composition.getCandidates() );
            evolution.getBindings().addAll( composition.getBindings() );
            
            System.out.println( "   candidate..: " + composition.getCandidates() );
            System.out.println( "   binding....: " + composition.getBindings() );
            
            evolutions.add( evolution );
            System.out.println( "   added!" );
        }
        
        System.out.println( "Evolutions Number: " + evolutions.size() );
        
        return evolutions;
    }
    
    private void print()
    {
        System.out.print( "[STEP 1] options: " + options.getDeclaration() );
        System.out.print( "       funcionalities: " + options.getFuncionalities() );
        System.out.println();

        for( AutomaticOption option : options.getComponents() )
        {
            System.out.println( "" );
            System.out.println( "     candidate......: " + option.getComponent() );
            System.out.println( "     funcionalities.: " + FuncionalityUtils.list( option.getComponent() ) );
            System.out.println( "     bindings.......: " + option.getBindings()  );
        }

        System.out.println( "---" );
    }
    
    private Collection<AutomaticEvolution> create()
    {
        Set<AutomaticEvolution> compositions = new LinkedHashSet<>();
        
        for( AutomaticOption option : options.getComponents() )
        {
            compositions.addAll( create( option ) );
        }
        
        return compositions;
    }
    
    private List<AutomaticEvolution> create( AutomaticOption option )
    {
        Set<String> funcCommnons = FuncionalityUtils.commons( 
            option.getComponent() , 
            options.getFuncionalities()
        );
        
        AutomaticEvolution original = new AutomaticEvolution( 
            options.getDeclaration().getParent() 
        );

        original.getCandidates().add( new Candidate( 
            option.getComponent() , 
            funcCommnons
        ) );
        
        // if it has all funcionality, it is completed!
        if( funcCommnons.size() == options.getFuncionalities().size() )
        {
            return create( original , option );
        }
        // if not, it is necessary to find another component to complete it!
        else
        {
            List<AutomaticEvolution> results = new LinkedList<>();
            
            for( AutomaticEvolution duplicated : create(original , option ) )
            {
                results.addAll( create( duplicated ) );
            }
            
            return results;
        }
    }
    
    private List<AutomaticEvolution> create( AutomaticEvolution original )
    {
        // functionality avaliable
        Set<String> avaliable = FuncionalityUtils.available( 
            options.getFuncionalities() , 
            original.getCandidates() 
        );
        
        List<AutomaticEvolution> evolutions = new LinkedList<>();
        
        for( AutomaticOption other : options.getComponents() )
        {
            if( !FuncionalityUtils.hasOneThisFuncionalities( other.getComponent() , avaliable ) )
            {
                continue ;
            }
            
            Set<String> funcCommnons = FuncionalityUtils.commons( 
                other.getComponent() , 
                avaliable
            );
            
            AutomaticEvolution cloned = original.clone();
            cloned.getCandidates().add( new Candidate( 
                other.getComponent() , 
                funcCommnons
            ) );
            
            // if it has all funcionality, it is completed!
            if( funcCommnons.size() == avaliable.size() )
            {
                evolutions.addAll( create( cloned , other ) );
            }
            // if not, it is necessary to find another component to complete it!
            else
            {
                for( AutomaticEvolution duplicated : create( cloned , other ) )
                {
                    evolutions.addAll( create( duplicated ) );
                }
            }
        }
        
        return evolutions;
    }
    
    private List<AutomaticEvolution> create( AutomaticEvolution original , AutomaticOption option )
    {
        List<AutomaticEvolution> compositions = new LinkedList<>();
        
        for( List<AutomaticBinding> bindings : option.getBindings() )
        {
            AutomaticEvolution cloned = original.clone();
            
            for( AutomaticBinding b : bindings )
            {
                addBinding( cloned , b );
            }
            
            compositions.add( cloned );
        }
        
        return compositions;
    }
    
    private void addBinding( AutomaticEvolution cloned , AutomaticBinding b )
    {
        try
        {
            System.out.println( "   binding: " + b );
            
            switch ( BindingUtils.isCompatible( b ) )
            {
                case BindingUtils.TYPE_COMPATIBLE:
                    System.out.println( "                   is COMPATIBLE." );
                    
                    cloned.getBindings().add( b );
                    break;
                case BindingUtils.TYPE_INCOMPATIBLE:
                    System.out.println( "                   is INCOMPATIBLE." );
                    
                    cloned.getBindings().add( b );
                    cloned.setStatus( AutomaticEvolution.STATUS_IGNORED );
                    cloned.getMessages().add( "One of the connection is incompatible." );
                    break;
                case BindingUtils.TYPE_COMPATIBLE_WITH_WRAPPER:
                    // create the wrapper
                    System.out.println( "                   is COMPATIBLE with WRAPPER." );
                    
                    String connection = b.toString();
                    
                    if( connection.contains( ":" ) )
                    {
                        connection = connection.substring( connection.indexOf( ":" ) + 1 ).trim();
                    }
                    
                    cloned.getBindings().add( b );
                    cloned.setStatus( AutomaticEvolution.STATUS_WARNING );
                    cloned.getMessages().add( BindingUtils.isHardware( b )
                        ? "A Hardware Conversor was used in " + connection + "." 
                        : "A Wrapper was used in " + connection + "." 
                    );
                    
                    break;
                default:
                    System.out.println( "                   has an error." );
            }
        }
        catch( Exception err )
        {
            err.printStackTrace();
            
            cloned.setStatus( AutomaticEvolution.STATUS_IGNORED );
            cloned.getMessages().add( "ERROR: " + err.getMessage() );
        }
    }
    
    
    // -------------------------- //
    // -------------------------- //
    // -------------------------- //
    
    private void verifyFuncionatilies( AutomaticEvolution composition )
    {
        Set<String> avaliable = FuncionalityUtils.available( 
            options.getFuncionalities() , 
            composition.getCandidates()
        );

        if( !avaliable.isEmpty() )
        {
            composition.setStatus( AutomaticEvolution.STATUS_IGNORED );
            
            String message = avaliable.size() == 1
                ? "The funcionality {0} has no component related it."
                : "The funcionalities {0} have no components relate them.";
            
            composition.getMessages().add(  
                MessageFormat.format( message , BusUtils.toString( avaliable ) )
            );
        }
    }
    
}
