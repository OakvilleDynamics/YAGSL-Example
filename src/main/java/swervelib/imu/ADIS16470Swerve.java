package swervelib.imu;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Optional;

/** IMU Swerve class for the {@link ADIS16470_IMU} device. */
public class ADIS16470Swerve extends SwerveIMU {

  /** {@link ADIS16470_IMU} device to read the current headings from. */
  private final ADIS16470_IMU imu;

  /** Offset for the ADIS16470. */
  private Rotation3d offset = new Rotation3d();

  /**
   * Construct the ADIS16470 imu and reset default configurations. Publish the gyro to the
   * SmartDashboard.
   */
  public ADIS16470Swerve() {
    imu = new ADIS16470_IMU();
    offset = new Rotation3d();
    factoryDefault();
    SmartDashboard.putData(imu);
  }

  /** Reset IMU to factory default. */
  @Override
  public void factoryDefault() {
    offset =
        new Rotation3d(
            Math.toRadians(imu.getYComplementaryAngle()),
            Math.toRadians(imu.getXComplementaryAngle()),
            Math.toRadians(imu.getAngle()));
  }

  /** Clear sticky faults on IMU. */
  @Override
  public void clearStickyFaults() {
    // Do nothing.
  }

  /**
   * Set the gyro offset.
   *
   * @param offset gyro offset as a {@link Rotation3d}.
   */
  public void setOffset(Rotation3d offset) {
    this.offset = offset;
  }

  /**
   * Fetch the {@link Rotation3d} from the IMU without any zeroing. Robot relative.
   *
   * @return {@link Rotation3d} from the IMU.
   */
  public Rotation3d getRawRotation3d() {
    return new Rotation3d(
        Math.toRadians(imu.getYComplementaryAngle()),
        Math.toRadians(imu.getXComplementaryAngle()),
        Math.toRadians(imu.getAngle()));
  }

  /**
   * Fetch the {@link Rotation3d} from the IMU. Robot relative.
   *
   * @return {@link Rotation3d} from the IMU.
   */
  @Override
  public Rotation3d getRotation3d() {
    return getRawRotation3d().minus(offset);
  }

  /**
   * Fetch the acceleration [x, y, z] from the IMU in meters per second squared. If acceleration
   * isn't supported returns empty.
   *
   * @return {@link Translation3d} of the acceleration as an {@link Optional}.
   */
  @Override
  public Optional<Translation3d> getAccel() {
    return Optional.of(new Translation3d(imu.getAccelX(), imu.getAccelY(), imu.getAccelZ()));
  }

  /**
   * Get the instantiated IMU object.
   *
   * @return IMU object.
   */
  @Override
  public Object getIMU() {
    return imu;
  }
}
