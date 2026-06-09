// existing code...

public class PortTasksPlugin {
    // existing code...

    @Override
    public void onLoop() {
        // existing code...

        // Tag bounty task NPCs
        BountyTask bountyTask = new BountyTask();
        bountyTask.tagBountyTaskNPCs();
    }

    // existing code...
}