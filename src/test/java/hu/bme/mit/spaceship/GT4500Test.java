package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore pts = mock(TorpedoStore.class);
  private TorpedoStore sts = mock(TorpedoStore.class);

  @BeforeEach
  public void init(){
    this.ship = new GT4500(pts, sts);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(pts.isEmpty()).thenReturn(false);
    when(pts.fire(1)).thenReturn(true);  

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(pts, times(1)).isEmpty();
    verify(pts, times(1)).fire(1);  
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(pts.getTorpedoCount()).thenReturn(10);
    when(pts.fire(10)).thenReturn(true);  
    when(sts.getTorpedoCount()).thenReturn(10);
    when(sts.fire(10)).thenReturn(true); 

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(pts, times(1)).getTorpedoCount();
    verify(pts, times(1)).fire(10);  
    verify(sts, times(1)).getTorpedoCount();
    verify(sts, times(1)).fire(10);  
  }

  @Test
  public void GT4500_shouldFirePrimaryOnly_whenFirstFired(){
    // Arrange
    when(pts.isEmpty()).thenReturn(false);
    when(pts.fire(1)).thenReturn(true);  
    when(sts.isEmpty()).thenReturn(false);
    when(sts.fire(1)).thenReturn(true); 

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(pts, times(1)).fire(1);
    verify(sts, times(0)).fire(1); 
  }  

  @Test
  public void GT4500_shouldFireSecondaryOnly_whenFirstFiredAndPrimaryIsEmpty(){
    // Arrange
    when(pts.isEmpty()).thenReturn(true);
    //when(pts.fire(1)).thenReturn(false);  
    when(sts.isEmpty()).thenReturn(false);
    when(sts.fire(1)).thenReturn(true); 

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(pts, times(0)).fire(1);
    verify(sts, times(1)).fire(1); 
  }  

  @Test
  public void GT4500_shouldFireBothStores_whenFiredTwoTimes(){
    // Arrange
    when(pts.isEmpty()).thenReturn(false);
    when(pts.fire(1)).thenReturn(true);  
    when(sts.isEmpty()).thenReturn(false);
    when(sts.fire(1)).thenReturn(true); 

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result1);
    assertEquals(true, result2);
    verify(pts, times(1)).fire(1);
    verify(sts, times(1)).fire(1); 
  } 

  @Test
  public void GT4500_shouldFirePrimaryTwice_whenFiredTwoTimesAndSecondaryIsEmpty(){
    // Arrange
    when(pts.isEmpty()).thenReturn(false);
    when(pts.fire(1)).thenReturn(true);  
    when(sts.isEmpty()).thenReturn(true);
    //when(sts.fire(1)).thenReturn(false); 

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result1);
    assertEquals(true, result2);
    verify(pts, times(2)).fire(1);
    verify(sts, times(0)).fire(1); 
  } 

  @Test
  public void GT4500_shouldNotFireSecondary_whenPrimaryReportsError(){
    // Arrange
    when(pts.isEmpty()).thenReturn(false);
    when(pts.fire(1)).thenReturn(false);  
    when(sts.isEmpty()).thenReturn(false);
    when(sts.fire(1)).thenReturn(true); 

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result1);
    verify(pts, times(1)).fire(1);
    verify(sts, times(0)).fire(1); 
  } 

}
