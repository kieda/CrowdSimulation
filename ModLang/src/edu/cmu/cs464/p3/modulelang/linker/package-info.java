/**
 * Turns our ast into a tree of java classes. This stage does not actually 
 * instantiate any modules or check them for correctness, however, if we notice
 * that a class does not exist, we throw a {@link ModLangLinkingError}
 */
/**
 * @author zkieda
 */
package edu.cmu.cs464.p3.modulelang.linker;