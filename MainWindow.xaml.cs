namespace Aborea_Handelssystem
{
    /// <summary>
    /// Interaktionslogik für MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private void RadioButtonMenge_Checked(object sender, RoutedEventArgs e)
        {
            int a = 1;
            try
            {
                a = Int32.Parse(((RadioButton)sender).Content.ToString());
            }
            catch(Exception)
            {
                a = 1;
            }
            if (controller != null)
            {
                controller.Menge = a;
                if(controller.SelectedSpielerIndex >= 0)
                    SetSpieler(controller.GeladeneSpieler[controller.SelectedSpielerIndex].Name);
                if(controller.SelectedStadtIndex >= 0)
                    SetStadt(controller.Städte[controller.SelectedStadtIndex].Name);
            }
        }

        private void ButtonGut_Click(object sender, RoutedEventArgs e)
        {
            if (DataGridWaren.SelectedIndex < 0)
                return;
            if (controller.SelectedSpielerIndex < 0)
                return;
            if (controller.SelectedStadtIndex < 0)
                return;
            Button s = (Button)sender;
            bool kauf = s.Content.ToString() == "Kauf";
            controller.Handel(controller.Waren[DataGridWaren.SelectedIndex].Ware, kauf);
            if (controller.SelectedSpielerIndex >= 0)
                SetSpieler(controller.GeladeneSpieler[controller.SelectedSpielerIndex].Name);
            if (controller.SelectedStadtIndex >= 0)
                SetStadt(controller.Städte[controller.SelectedStadtIndex].Name);
        }

        private void MenuItemProduction_Click(object sender, RoutedEventArgs e)
        {
            controller.Produziere();
            ComboBoxStadt_SelectionChanged(sender, null);
            ComboBoxSpieler_SelectionChanged(sender, null);
        }
    }
}
