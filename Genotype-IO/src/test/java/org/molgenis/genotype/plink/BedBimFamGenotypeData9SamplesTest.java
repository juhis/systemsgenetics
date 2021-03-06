package org.molgenis.genotype.plink;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.ResourceTest;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.util.Utils;
import org.molgenis.genotype.variant.GeneticVariant;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The same as the PED/MAP test, except on the BED/BIM/FAM backend
 * 
 * @author jvelde
 * 
 */
public class BedBimFamGenotypeData9SamplesTest extends ResourceTest
{
	private BedBimFamGenotypeData genotypeData;

	@BeforeClass
	public void beforeClass() throws Exception
	{
		genotypeData = new BedBimFamGenotypeData(getTestBed9(), getTestBim9(), getTestFam9(), 0);
	}

	@Test
	public void getSeqNames()
	{
		List<String> seqNames = genotypeData.getSeqNames();
		assertNotNull(seqNames);
		assertEquals(seqNames.size(), 2);
		assertEquals(seqNames.get(0), "22");
		assertEquals(seqNames.get(1), "23");
	}

	@Test
	public void testGetSequences()
	{
		Iterable<Sequence> sequences = genotypeData.getSequences();
		assertNotNull(sequences);
		int count = 0;
		for(Sequence s : sequences){
			++count;
		}
		assertEquals(count, 2);
	}

	@Test
	public void testGetSequenceByName() throws IOException
	{
		Sequence sequence = genotypeData.getSequenceByName("22");
		assertNotNull(sequence);
		assertEquals(sequence.getName(), "22");

		List<GeneticVariant> variants = Utils.iteratorToList(genotypeData.getSequenceGeneticVariants(sequence.getName()).iterator());
		assertNotNull(variants);
		assertEquals(variants.size(), 9);
		GeneticVariant variant = variants.get(0);
		assertEquals(variant.getPrimaryVariantId(), "rs11089130");
		assertEquals(variant.getStartPos(), 14431347);

		assertEquals(variant.getSequenceName(), "22");
		assertEquals(variant.isSnp(), true);

		List<String> alleles = variant.getVariantAlleles().getAllelesAsString();
		assertNotNull(alleles);
		assertEquals(alleles.size(), 2);
		assertTrue(alleles.contains("C"));
		assertTrue(alleles.contains("G"));

		List<Alleles> sampleVariants = variant.getSampleVariants();
		assertNotNull(sampleVariants);
		assertEquals(sampleVariants.size(), 9);
		assertNotNull(sampleVariants.get(0).getAllelesAsChars());
		assertEquals(sampleVariants.get(0).getAlleles().size(), 2);
		assertEquals(sampleVariants.get(0).getAllelesAsChars()[0], 'C');
		assertEquals(sampleVariants.get(0).getAllelesAsChars()[1], 'C');
		assertNotNull(sampleVariants.get(1).getAllelesAsChars());
		assertEquals(sampleVariants.get(1).getAllelesAsChars().length, 2);
		assertEquals(sampleVariants.get(1).getAllelesAsChars()[0], 'G');
		assertEquals(sampleVariants.get(1).getAllelesAsChars()[1], 'C');
		assertNotNull(sampleVariants.get(2).getAllelesAsChars());
		assertEquals(sampleVariants.get(2).getAllelesAsChars().length, 2);
		assertEquals(sampleVariants.get(2).getAllelesAsChars()[0], 'G');
		assertEquals(sampleVariants.get(2).getAllelesAsChars()[1], 'G');
	}

	@Test
	public void testGetSamples()
	{
		List<Sample> samples = genotypeData.getSamples();
		assertNotNull(samples);
		assertEquals(samples.size(), 9);
		assertEquals(samples.get(0).getId(), "1042");
		assertEquals(samples.get(0).getFamilyId(), "F1042");
	}

	@Test
	public void testGetSamplePhasing()
	{
		Iterable<GeneticVariant> variants = genotypeData.getVariantsByPos("22", 14431347);
		
		int count = 0;
		for(GeneticVariant variant : variants){
			assertEquals(variant.getSamplePhasing(), Arrays.asList(false, false, false, false, false, false, false, false, false));
			++count;
		}
		assertEquals(count, 1);
	}

	@Test
	public void testGetSnpVariantByPos()
	{
		int pos = 14434960;
		GeneticVariant variant = genotypeData.getSnpVariantByPos("23", pos);
		assertNotNull(variant);
		assertEquals(variant.getStartPos(), pos);
	}
	
	@Test
	public void testGetSnpVariantByPos2(){
		int pos = 14434961;
		GeneticVariant variant = genotypeData.getSnpVariantByPos("23", pos);
		assertNull(variant);
	}
	
	@Test
	public void testGetSnpVariantByPos3(){
		int pos = 14434961;
		GeneticVariant variant = genotypeData.getSnpVariantByPos("24", pos);
		assertNull(variant);
	}
	
	@Test
	public void testGetSnpVariantByPos4(){
		int pos = 14434960;
		GeneticVariant variant = genotypeData.getSnpVariantByPos("24", pos);
		assertNull(variant);
	}

}
