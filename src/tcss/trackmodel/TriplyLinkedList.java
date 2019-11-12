package tcss.trackmodel;

import java.util.HashMap;
import java.util.Map;

public class TriplyLinkedList {



    /* class TLLNode */
    private class TLLNode
    {
        TLLNode head, tail, branch;
        Block block;

        /* Constructor */
        public TLLNode(Block b)
        {
            block = b;
            head = null;
            tail = null;
            branch = null;
        }
    }
}
