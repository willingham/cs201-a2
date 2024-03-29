package trees;

import helpers.Queue;



public class Rbt extends Bst {
    protected class Node extends Bst.Node {
        boolean color = true;

        public Node(String s) {
            super(s);
        }

        public boolean getColor() {
            return this.color;
        }

        public void setColor(boolean c) {
            this.color = c;
        }

        public boolean isRed() {
            return (this.color == true);
        }

        public void setColorRed() {
            this.color = true;
        }

        public boolean isBlack() {
            return (this.color == false);
        }

        public void setColorBlack() {
            this.color = false;
        }

        public Node getGrandparent() {
            if (this == null || getParent() == null || getParent().getParent() == getParent()) {
                return null;
            } else {
                return (Node)getParent().getParent();
            }
        }

        public Node getUncle() {
            if (this == null || getGrandparent() == null) {
                return null;
            } else if (getGrandparent().getLeftChild() == getParent()) {
                return (Node)getGrandparent().getRightChild();
            } else {
                return (Node)getGrandparent().getLeftChild();
            }
        }

        public Node getSibling() {
            if (this == null || getParent() == null) {
                return null;
            } else if (getParent().getLeftChild() == this) {
                return (Node)getParent().getRightChild();
            } else {
                return (Node)getParent().getLeftChild();
            }
        }

        public void rotateLeft() {
            if (this.getRightChild() == null) {
                return;
            }
            Node curr = this;
            Node prev = ((Node)this.getRightChild());
            curr.setRightChild(prev.getLeftChild());
            if (curr.getParent() == null || curr.getParent() == curr) {
                setRootNode(prev);
            } else if (curr.getParent().getLeftChild() == curr) {
                curr.getParent().setLeftChild(prev);
            } else {
                curr.getParent().setRightChild(prev);
            }
            prev.setLeftChild(curr);
        }

        public void rotateRight() {
          if (this.getLeftChild() == null) {
              return;
          }
          Node curr = this;
          Node prev = ((Node)this.getLeftChild());
          curr.setLeftChild(prev.getRightChild());
          if (curr.getParent() == null || curr.getParent() == curr) {
              setRootNode(prev);
          } else if (curr.getParent().getLeftChild() == curr) {
              curr.getParent().setLeftChild(prev);
          } else {
              curr.getParent().setRightChild(prev);
          }
          prev.setRightChild(curr);
        }

        public boolean childrenAreBlack() {
          boolean left = false;
          boolean right = false;
          if (this.hasNoChildren() == true) {
              return true;
          }
          if (this.getLeftChild() != null) {
              if (((Node) this.getLeftChild()).isBlack()) {
                  left = true;
              }
          } else {
              left = true;
          }
          if (this.getRightChild() != null) {
              if (((Node) this.getRightChild()).isBlack()) {
                  right = true;
              }
          } else {
              right = true;
          }
          if (left && right) {
              return true;
          } else {
              return false;
          }

        }


    }

    public Rbt() {
        super();
    }

    @Override
    public void insert(String s) {
        this.insert(new Node(s));
        return;
    }

    public void insert(Node n) {

        if (this.rootNode == null) {
            this.rootNode = n;
            ((Node) this.rootNode).setColorBlack();
            size++;
        } else {
            Node curr = ((Node)super.insert(n));
            if (curr.getFrequency() == 1) {
                insertionFixUp(curr);
            }
        }
    }

    public boolean isNotLinear(Node curr) {
    	if (curr.getParent() == null || curr.getGrandparent() == null) {
    		return false;
    	}
    	boolean lin = true;
    	if (curr.getParent().getLeftChild() == curr && curr.getGrandparent().getLeftChild() == curr.getParent()) {
    		lin = false;
    	} else if (curr.getParent().getRightChild() == curr && curr.getGrandparent().getRightChild() == curr.getParent()) {
    		lin = false;
    	}
    	return lin;
    }

    public void insertionFixUp(Node n) { //adapted from pseudocode on beastie http://beastie.cs.ua.edu/red-black/rbinsfix.html
        Node curr = n;
        curr.setColorRed();
        if (curr != null && rootNode != curr && ((Node)curr.getParent()).isRed()) {
            if (curr.getUncle() != null && curr.getUncle().isRed()) {
                ((Node)curr.getParent()).setColorBlack();
                curr.getUncle().setColorBlack();
                curr.getGrandparent().setColorRed();
                insertionFixUp(curr.getGrandparent());
            } else if (curr.getParent() == curr.getGrandparent().getLeftChild()) {
                if (curr == curr.getParent().getRightChild()) {
                    curr = ((Node)curr.getParent());
                    curr.rotateLeft();
                }
                ((Node)curr.getParent()).setColorBlack();
                curr.getGrandparent().setColorRed();
                curr.getGrandparent().rotateRight();
            } else if (curr.getParent() == curr.getGrandparent().getRightChild()) {
                if (curr == curr.getParent().getLeftChild()) {
                    curr = ((Node)curr.getParent());
                    curr.rotateRight();
                }
                ((Node)curr.getParent()).setColorBlack();
                if (curr.getGrandparent() != null) {
                    curr.getGrandparent().setColorRed();
                    curr.getGrandparent().rotateLeft();
                }
            }
        }
        ((Node)rootNode).setColorBlack();
    }

    @Override
    public void delete(String s) {
        size--;
        Node curr = (Node) getNode(s);
        if (curr == null) {
            size++;
            System.err.printf("Error: '" + s + "' does not exist in tree.\n");
            return;
        } else if (curr.getFrequency() > 1) {
            curr.decrementFrequency();
            size++;
            return;
        } else if (curr.getLeftChild() != null && curr.getRightChild() != null) {
            Node predecessor = ((Node) curr.getPredecessor());
            curr.setValue(predecessor.getValue());
            curr.setFrequency(predecessor.getFrequency());
            curr = predecessor;
        }
        Node move = null;
        if (curr.getLeftChild() == null) {
            move = ((Node) curr.getRightChild());
        } else {
            move = ((Node) curr.getLeftChild());
        }
        if (move != null) {
            if (curr == rootNode) {
                setRootNode(move);
            } else if (curr.getParent().getLeftChild() == curr) {
                curr.getParent().setLeftChild(move);
            } else {
                curr.getParent().setRightChild(move);
            }
            if (curr.isBlack()) {
                deletionFixUp(move);
            }
        } else if (curr == rootNode) {
            rootNode = null;
        } else {
            if (curr.isBlack()) {
                deletionFixUp(curr);
            }
           curr.prune();
        }
    }

    public void deletionFixUp(Node x) { // adapted from pseudocode on wikipedia, https://en.wikipedia.org/wiki/Red–black_tree
        while (x != rootNode && !(x.isRed())) {
            if (x.getParent().getLeftChild() == x) {
                //Node sibling = x.getSibling();
                Node sibling = (Node) x.getParent().getRightChild();
                if (sibling.isRed()) {
                    sibling.setColorBlack();
                    ((Node) x.getParent()).setColorRed();
                    ((Node) x.getParent()).rotateLeft();
                    sibling = (Node) x.getParent().getRightChild();
                }
                if (sibling.childrenAreBlack()) {
                    sibling.setColorRed();
                    x = ((Node) x.getParent());
                } else {
                    if (((Node) sibling.getRightChild()).isBlack()) {
                        ((Node) sibling.getLeftChild()).setColorBlack();
                        sibling.setColorRed();
                        sibling.rotateRight();
                        sibling = (Node) x.getParent().getRightChild();
                    }
                    sibling.setColor(((Node) x.getParent()).getColor());
                    ((Node)  x.getParent()).setColorBlack();
                    ((Node) sibling.getRightChild()).setColorBlack();
                    ((Node) x.getParent()).rotateLeft();
                    x = ((Node) rootNode);
                }
            } else {
                Node sibling = (Node) x.getParent().getLeftChild();
                if (sibling.isRed()) {
                    sibling.setColorBlack();
                    ((Node) x.getParent()).setColorRed();
                    ((Node) x.getParent()).rotateRight();
                    sibling = (Node) x.getParent().getLeftChild();
                }
                if (sibling.childrenAreBlack()) {
                    sibling.setColorRed();
                    x = ((Node) x.getParent());
                } else {
                    if (sibling.getLeftChild() == null || ((Node) sibling.getLeftChild()).isBlack()) {
                        ((Node) sibling.getRightChild()).setColorBlack();
                        sibling.setColorRed();
                        sibling.rotateLeft();
                        sibling = x.getSibling();
                    }
                    sibling.setColor(((Node) x.getParent()).getColor());
                    ((Node) x.getParent()).setColorBlack();
                    ((Node) sibling.getLeftChild()).setColorBlack();
                    ((Node) x.getParent()).rotateRight();
                    x = ((Node) rootNode);
                }
            }
        }
        x.setColorBlack();
    }

    @Override
    public void print() {
        Queue q = new Queue();
        Queue p = new Queue();
        if (rootNode == null) { return; }
        q.add((Node)rootNode);
        Node parent = null;
        int lineNumber = 1;
        while (!q.isEmpty()) {
            int count = q.size();
            p.add(String.valueOf(lineNumber) + ": ");
            while (count > 0) { //process all values at current level
                Node curr = (Node) q.remove();
                if (curr.getLeftChild() == null && curr.getRightChild() == null) {
                    p.add("=");
                }
                if (rootNode == curr) {
                	parent = ((Node) rootNode);
                } else {
                	parent = ((Node) curr.getParent());
                }
                p.add((String) curr.getValue());
                if (curr.isRed()) {
                    p.add("*");
                }
                p.add("(" + ((String) parent.getValue()));
                if (parent.isRed()) {
                    p.add("*");
                }
                p.add(")" + String.valueOf(curr.getFrequency()));
                if (curr == rootNode) {
                    p.add("X");
                } else if (curr.getParent().getLeftChild() == curr) {
                    p.add("L ");
                } else if (curr.getParent().getRightChild() == curr) {
                    p.add("R ");
                }
                if (curr.getLeftChild() != null) {
                    q.add((Node)curr.getLeftChild());
                }
                if (curr.getRightChild() != null) {
                    q.add((Node)curr.getRightChild());
                }
                count--;
            }
            p.add("\n");
            while (!p.isEmpty()) {
                System.out.print(String.valueOf(p.remove()));
            }
            lineNumber += 1;
        }
        return;
    }
}
