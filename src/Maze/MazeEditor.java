package Maze;

import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;
import java.util.Date;

import static Maze.Maze.mazeEntrance;
import static Maze.Maze.mazeExit;
import static Maze.Maze.showSolution;
/** This is a class for the maze editor which the user interacts with, its properties are made up of
 * individual private GUI elements which are used to get user input, The current maze being editted
 * is also held in a Maze field named currentMaze
 */

public class MazeEditor implements ActionListener {
    private JTextArea sizeTextBoxX, sizeTextBoxY, authorTextArea, nameTextArea;
    private JButton clearBtn, vWallBtn, hWallBtn, drawBtn, eraseBtn;
    private JScrollPane imageScrollPane;
    private JCheckBox solutionCheckBox;
    private JFrame editorFrame;
    private Maze currentMaze;
    private MazeDrawer mazeDrawer;
    private ArrayList<JCheckBox> selection;
    private JSpinner ImageXJSpinner, ImageYJSpinner;

    private static final int frameWidth = 1250;
    private static final int frameHeight = 950;
    private static final int defaultWidth = 100;
    private static final int defaultHeight = 100;
    private static int offsetX = 0;
    private static int offsetY = 0;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clearBtn) {
                mazeDrawer.clear();
            } else if (e.getSource() == vWallBtn) {
                mazeDrawer.verticalWall();
            } else if (e.getSource() == hWallBtn) {
                mazeDrawer.horizontalWall();
            } else if (e.getSource() == drawBtn) {
                mazeDrawer.draw();
            } else if (e.getSource() == eraseBtn) {
                mazeDrawer.erase();
            }
        }
    };

    /** This method creates the GUI user and sets it to visible. From here
     * the user can interact with the buttons and text boxes and use them
     * as inputs. The current maze property is also instantiated here
     * so that there is a preloaded maze for the user of size 70x50
     */
    public void createGUI() throws Exception {
        this.selection = new ArrayList<>();
        this.editorFrame = new JFrame("Maze Editor");
        BorderLayout borderLayout = new BorderLayout();
        editorFrame.setLayout(borderLayout);
        editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editorFrame.setSize(frameWidth, frameHeight);

        //Components for East panel
        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        eastPanel.setLayout(new GridLayout(12,1));

        Button generateMazeButton = new Button("Generate");
        generateMazeButton.setPreferredSize(new Dimension(150,30));
        generateMazeButton.addActionListener(this);
        eastPanel.add(generateMazeButton);

        JLabel sizeLabelX = new JLabel("Size X");
        eastPanel.add(sizeLabelX);

        this.sizeTextBoxX = new JTextArea("100");
        eastPanel.add(sizeTextBoxX);
        sizeTextBoxX.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel sizeLabelY = new JLabel("Size Y");
        eastPanel.add(sizeLabelY);

        this.sizeTextBoxY = new JTextArea("100");
        eastPanel.add(sizeTextBoxY);
        sizeTextBoxY.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel exportAsImageLabel = new JLabel("Export as Image");
        JButton exportAsImageButton = new JButton("Export");
        exportAsImageButton.addActionListener(this);
        eastPanel.add(exportAsImageLabel);
        eastPanel.add(exportAsImageButton);

        JLabel databasePreviewLabel = new JLabel("Database Mazes");
        JScrollPane databasePreviewScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        databasePreviewScrollPane.setPreferredSize(new Dimension(30,400));
        eastPanel.add(databasePreviewLabel);
        eastPanel.add(databasePreviewScrollPane);

        //Components for West Panel
        JPanel westPanel = new JPanel();
        westPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        westPanel.setLayout(new GridLayout(16,1));

        JLabel authorLabel = new JLabel("Author:");
        westPanel.add(authorLabel);
        authorLabel.setPreferredSize(new Dimension(150,50));

        this.authorTextArea = new JTextArea();
        westPanel.add(this.authorTextArea);
        authorTextArea.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel nameLabel = new JLabel("Name:");
        westPanel.add(nameLabel);

        this.nameTextArea = new JTextArea();
        westPanel.add(this.nameTextArea);
        nameTextArea.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel solverLabel = new JLabel("Solver Tools");
        westPanel.add(solverLabel);

        JButton importButton = new JButton("import maze");
        importButton.addActionListener(this);
        westPanel.add(importButton);

        JLabel optimalSolutionLabel = new JLabel("Optimal Solution(%): no maze imported");
        westPanel.add(optimalSolutionLabel);

        JLabel deadEndLabel = new JLabel("Dead ends (%): no maze imported");
        westPanel.add(deadEndLabel);

        this.solutionCheckBox = new JCheckBox("Toggle solution");
        this.solutionCheckBox.addActionListener(this);
        westPanel.add(solutionCheckBox);

        JLabel addImageLabel = new JLabel("Images");
        this.imageScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JButton imageImportButton = new JButton("Add Image");
        imageImportButton.addActionListener(this);

        //CODE FROM https://stackoverflow.com/questions/30239318/jbutton-with-padding-between-its-border-and-the-button-itself
        imageImportButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(editorFrame.getBackground(), 5),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JLabel ImageXLabel = new JLabel("Image X coordinate");
        this.ImageXJSpinner = new JSpinner();
        JLabel ImageYLabel = new JLabel("Image Y coordinate");
        this.ImageYJSpinner = new JSpinner();

        westPanel.add(ImageXLabel);
        westPanel.add(ImageXJSpinner);
        westPanel.add(ImageYLabel);
        westPanel.add(ImageYJSpinner);

        westPanel.add(addImageLabel);
        westPanel.add(imageImportButton);
        westPanel.add(imageScrollPane);

        //South panel components
        JPanel southPanel = new JPanel();

        JButton exportButton = new JButton("Export to database");
        exportButton.setPreferredSize(new Dimension(200,50));
        exportButton.addActionListener(this);
        southPanel.add(exportButton);

        JButton editButton = new JButton("Edit Maze");
        editButton.setPreferredSize(new Dimension(200,50));
        editButton.addActionListener(this);
        southPanel.add(editButton);

        editorFrame.add(eastPanel, BorderLayout.EAST);
        editorFrame.add(westPanel, BorderLayout.WEST);
        editorFrame.add(southPanel, BorderLayout.SOUTH);

        //add empty top panel
        JPanel topPanel = new JPanel();
        editorFrame.add(topPanel,BorderLayout.NORTH);

        JFrame imageModal = new JFrame();
        imageModal.setSize(700, 600);

        //maze cant be empty so create a random maze for user to start with
        this.currentMaze = new Maze(defaultWidth, defaultHeight);
        this.currentMaze.createGrid();
        this.currentMaze.randomGenerate();
        Solver solver = new Solver();
        solver.showSolution(mazeEntrance, mazeExit, currentMaze.maze);
        this.currentMaze.setBorder(new EmptyBorder(10, 0, 0, 0));
        editorFrame.add(this.currentMaze, BorderLayout.CENTER);

        //centres the frame on the screen
        editorFrame.setLocationRelativeTo(null);
        //makes gui visible
        editorFrame.setVisible(true);

        JPanel controls = new JPanel();
        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(actionListener);

        vWallBtn = new JButton("Verticle line");
        vWallBtn.addActionListener(actionListener);

        hWallBtn = new JButton("Horizontal Line");
        hWallBtn.addActionListener(actionListener);

        drawBtn = new JButton("Draw");
        drawBtn.addActionListener(actionListener);

        eraseBtn = new JButton("Erase");
        eraseBtn.addActionListener(actionListener);

        controls.add(drawBtn);
        controls.add(eraseBtn);
        controls.add(vWallBtn);
        controls.add(hWallBtn);
        controls.add(clearBtn);

        editorFrame.add(controls, BorderLayout.NORTH);
    }

    /** This actionlistener picks up whenever an action is performed and uses a switch statement to check
     * what specific component has been interacted with and then performs a gui function based on the
     * component's purpose
     */
    //method from https://stackoverflow.com/questions/1667060/can-i-use-switch-case-in-actionperformed-method-in-java
    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()) {
            case "Generate":
                try {
                    this.generateMaze();
                }
                catch (Exception exception){
                    errorMessage(exception.getMessage());
                }
                break;
            case "Export to database":
                try {
                    this.exportMaze();
                } catch (Exception exception) {
                    errorMessage(exception.getMessage());
                }
                break;
            case "Export":
                System.out.println("export these");

                int index = 0;
                ArrayList<Integer> indexs = new ArrayList<>();
                for (JCheckBox element : this.selection) {
                    if(element.isSelected()){ indexs.add(index);}
                    index++;
                }
                ResultSet resultSet;
                ArrayList<BufferedImage> imageList = new ArrayList<>();
                try {
                    Connection connection = DBConnection.getInstance();
                    Statement st = connection.createStatement();
                    resultSet = st.executeQuery("SELECT Image FROM Mazes;");

                    //CODE FROM https://mkyong.com/java/how-to-convert-byte-to-bufferedimage-in-java/
                    while (resultSet.next()) {
                        byte[] byteArray = resultSet.getBytes("Image");

                        ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
                        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
                        ImageIcon bi = (ImageIcon) objectStream.readObject();

                        //CODE FROM https://stackoverflow.com/questions/15053214/converting-an-imageicon-to-a-bufferedimage
                        BufferedImage buffimage = new BufferedImage(
                                bi.getIconWidth(),
                                bi.getIconHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        Graphics g = buffimage.createGraphics();
                        // paint the Icon to the BufferedImage.
                        bi.paintIcon(null, g, 0,0);
                        g.dispose();

                        System.out.println(bi);
                        imageList.add(buffimage);
                    }
                }
                catch (SQLException ignored){} catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();}

                System.out.println(indexs);
                //code from https://stackoverflow.com/questions/10083447/selecting-folder-destination-in-java
                //https://tips4java.wordpress.com/2008/10/13/screen-image/
                JFileChooser chooser = getjFileChooser();
                //
                int a = 0;
                for(BufferedImage IMAGE: imageList) {
                    if (indexs.contains(a)) {

                        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            System.out.println("getCurrentDirectory(): "
                                    + chooser.getSelectedFile());

                            String filePath = chooser.getSelectedFile() + "slash" + "Maze" + a + ".jpg";
                            filePath = filePath.replaceAll("slash", "\\\\");
                            System.out.println(filePath);
                            try {
                                ImageIO.write(IMAGE, "jpg", new File(filePath));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            System.out.println("No Selection ");
                        }
                    }
                    a++;
                }
                break;
            case "import maze":
                System.out.println("function Import maze");
                break;
            case "Toggle solution":
                if(this.solutionCheckBox.isSelected()){
                    this.showSolOn();
                }
                else{
                    this.showSolOff();
                }
                editorFrame.revalidate();
                editorFrame.repaint();
                break;
            case "Add Image":
                //method obtained from https://www.youtube.com/watch?v=A6sA9KItwpY
                //file filter method from https://www.codejava.net/java-se/swing/add-file-filter-for-jfilechooser-dialog
                JFileChooser imageSelector = new JFileChooser();
                imageSelector.addChoosableFileFilter(new FileNameExtensionFilter("Images", "png","jpg","jpeg"));
                imageSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
                imageSelector.setAcceptAllFileFilterUsed(true);

                int response = imageSelector.showOpenDialog(null);

                if (response == JFileChooser.APPROVE_OPTION) {
                    File file = new File(imageSelector.getSelectedFile().getAbsolutePath());
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(file);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    if(img != null)
                    {
                        try {
                            currentMaze.addImage(img,(int)(ImageXJSpinner.getValue())*Cell.cellSize*currentMaze.getZoomIndex(),(int)(ImageYJSpinner.getValue())*Cell.cellSize*currentMaze.getZoomIndex());
                        } catch (Exception ex) {
                            errorMessage(ex.getMessage());
                        }
                        currentMaze.repaint();
                    }

                    assert img != null;
                    JLabel pic = new JLabel(new ImageIcon(img.getScaledInstance(100,100, Image.SCALE_SMOOTH)));
                    this.imageScrollPane.getViewport().add(pic);
                    this.imageScrollPane.setSize(240,100);

                }
                else{
                    break;
                }
                break;

            case "Edit Maze":
                try {
                    editMode();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                //pass
        }
    }

    private static JFileChooser getjFileChooser() {
        JFileChooser chooser;
        String choosertitle = "";
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser;
    }

    /** This method generates a randomized maze which size is dependent on the users inputs
     * in the sizeTextBoxX and sizeTextBoxY JTextArea's
     */
    public void generateMaze () throws Exception {
        //Check if X and Y are empty and throw exception
        if((this.sizeTextBoxX.getText().isEmpty()) || (this.sizeTextBoxY.getText().isEmpty()))
        {throw new Exception("X and Y cannot be empty");}

        int currentWidth = Integer.parseInt(this.sizeTextBoxX.getText());
        int currentHeight = Integer.parseInt(this.sizeTextBoxY.getText());

        //check if maze is out of bounds and throw exception if true
        if ((currentHeight > 100)
                ||(currentWidth > 100)
                ||(currentHeight < 1)
                ||(currentWidth < 1))
        {throw new Exception("Size out of bounds");}

        if (currentMaze != null) {
            editorFrame.remove(currentMaze);
        }

        if (mazeDrawer != null) {
            editorFrame.remove(mazeDrawer);
        }

        if (currentWidth < 100 && currentHeight < 100) {
            if (currentWidth < 90) {
                offsetX = (defaultWidth - currentWidth) * (currentWidth / 10);
            }

            if (currentHeight < 90) {
                offsetY = (defaultHeight - currentHeight) * (currentHeight / 10);
            }

            editorFrame.setSize(frameWidth, frameHeight);
        } else {
            editorFrame.setSize(frameWidth, frameHeight);
        }

        this.currentMaze = new Maze(currentWidth, currentHeight);
        editorFrame.add(this.currentMaze,BorderLayout.CENTER);
        this.currentMaze.createGrid();
        this.currentMaze.randomGenerate();
        Solver solver = new Solver();
        solver.showSolution(mazeEntrance, mazeExit, currentMaze.maze);
        editorFrame.revalidate();
        editorFrame.repaint();
    }

    /**
     * This methods opens a window which then allows the user to interact and create their own maze with a paint canvas
     */
    public void editMode() throws Exception {
        //Check if X and Y are empty and throw exception
        if((this.sizeTextBoxX.getText().isEmpty()) || (this.sizeTextBoxY.getText().isEmpty()))
        {throw new Exception("X and Y cannot be empty");}

        int currentWidth = Integer.parseInt(this.sizeTextBoxX.getText());
        int currentHeight = Integer.parseInt(this.sizeTextBoxY.getText());

        //check if maze is out of bounds and throw exception if true
        if ((currentHeight > 100)
                ||(currentWidth > 100)
                ||(currentHeight < 1)
                ||(currentWidth < 1))
        {throw new Exception("Size out of bounds");}

        if (currentMaze != null) {
            editorFrame.remove(currentMaze);
        }

        if (mazeDrawer != null) {
            editorFrame.remove(mazeDrawer);
        }

        if (currentWidth < 100 && currentHeight < 100) {
            offsetX = (defaultWidth - currentWidth) * (currentWidth / 10);

            offsetY = (defaultHeight - currentHeight) * (currentHeight / 10);

            editorFrame.setSize(frameWidth - offsetX, frameHeight - offsetY);
        } else {
            editorFrame.setSize(frameWidth, frameHeight);
        }

        this.mazeDrawer = new MazeDrawer(currentWidth, currentHeight);
        editorFrame.add(this.mazeDrawer, BorderLayout.CENTER);
        editorFrame.revalidate();
        editorFrame.repaint();

    }

    /** This method exports a maze with a name and author attached to it, these are obtained from
     * the user inputs in authorTextArea and nameTextArea JTextArea's
     */
    public void exportMaze() throws Exception {
        //Check if name and author are not empty
        if((this.nameTextArea.getText().isEmpty())){throw new Exception("Name can't be blank");}
        if((this.authorTextArea.getText().isEmpty())){throw new Exception("Author can't be blank");}

        //CODE FROM https://beginnersbook.com/2017/10/java-display-time-in-12-hour-format-with-ampm/#:~:text=There%20are%20two%20patterns%20that,hour%20format%20with%20AM%2FPM.&text=aa%20%E2%80%93%20AM%2FPM%20marker
        //CODE FROM https://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java
        DateFormat Time = new SimpleDateFormat("hh:mm aa");
        String currentTime = Time.format(new Date());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        String currentDate= dateFormat.format(cal.getTime());

        //CODE FROM CAB302 WEEK 6 SLIDE 39

        BufferedImage mazeImage = ScreenImage.createImage(this.currentMaze);
        ImageIcon imageIconMazeImage = new ImageIcon(mazeImage);
        // ImageIcons are Serializable
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        // serialize the ImageIcon and create a byte array for storage
        objectStream.writeObject(imageIconMazeImage);
        byte[] data = byteStream.toByteArray();

        Connection connection =  DBConnection.getInstance();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Mazes VALUES (?, ?, ?, ?, ?, ?);");
        statement.clearParameters();
        statement.setString(1, this.nameTextArea.getText());
        statement.setString(2, this.authorTextArea.getText());
        statement.setString(3, "3");
        statement.setString(4, currentDate);
        statement.setString(5, currentTime);
        statement.setBinaryStream(6,new ByteArrayInputStream(data), data.length);
        statement.executeUpdate();
    }

    /**
     * This method enables drawing of maze solution.
     */
    public void showSolOn(){
        showSolution = true;
    }

    /**
     * This method disables drawing of maze solution.
     */
    public void showSolOff() {
        showSolution = false;
    }

    /**
     * Used to create a popup error message for any exception thrown
     * @param message is a String that describes the response to relay to the user via a popup window
     */
    public void errorMessage(String message){
        JOptionPane.showMessageDialog(null, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) throws Exception {
        MazeEditor gui = new MazeEditor();
        gui.createGUI();
    }
}