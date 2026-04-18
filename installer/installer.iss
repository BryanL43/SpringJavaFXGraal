#define MyAppName "Example"
#define MyAppVersion "1.0.0"
#define MyAppExeName "example.exe"

[Setup]
SourceDir=..
OutputDir=target

AppName={#MyAppName}
AppVersion={#MyAppVersion}

// No admin privilege installation
PrivilegesRequired=lowest

// Enable selected pages
DisableWelcomePage=no
DisableFinishedPage=no

// Output properties
DefaultDirName={localappdata}\{#MyAppName}
DefaultGroupName={#MyAppName}
OutputBaseFilename={#MyAppName} Desktop Installer

// Compression properties
Compression=lzma2
SolidCompression=yes
LZMANumBlockThreads=2

// Assets
WizardImageFile=installer\assets\banner.bmp
WizardSmallImageFile=installer\assets\logo.bmp
SetupIconFile=installer\assets\logo.ico

LicenseFile=LICENSE

[Messages]
SetupWindowTitle={#MyAppName} {#MyAppVersion} Setup

WelcomeLabel1=Welcome to the {#MyAppName} {#MyAppVersion} Setup Wizard
WelcomeLabel2=The Setup Wizard will install {#MyAppName} {#MyAppVersion} on your computer. Click Next to continue, or Cancel to exit the Setup Wizard.
ClickNext=

ExitSetupMessage=Are you sure you want to cancel {#MyAppName} {#MyAppVersion} installation?

ReadyLabel2a=Click Install to begin the installation. Click Back to review or change any of your installation settings. Click Cancel to exit the wizard.

FinishedHeadingLabel=Completed the {#MyAppName} {#MyAppVersion} Setup Wizard

[Files]
Source: "target\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{userdesktop}\Example"; Filename: "{app}\example.exe"

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "Launch {#MyAppName}"; Flags: nowait postinstall skipifsilent

[Code]
var
  InterruptedPageActive: Boolean;

procedure AdjustWelcomePage;
var
  LeftOffset: Integer;
begin
  WizardForm.WizardBitmapImage.Width := ScaleX(165);

  // Calculate new left offset
  LeftOffset :=
    WizardForm.WizardBitmapImage.Left +
    WizardForm.WizardBitmapImage.Width +
    ScaleX(15);

  // Shift the Welcome page text left
  WizardForm.WelcomeLabel1.Left := LeftOffset;
  WizardForm.WelcomeLabel2.Left := LeftOffset;

  // Shift the Welcome page text down
  WizardForm.WelcomeLabel1.Top := WizardForm.WelcomeLabel1.Top + ScaleY(3);
  WizardForm.WelcomeLabel2.Top := WizardForm.WelcomeLabel2.Top + ScaleY(10);

  // Expand the Welcome page text width
  WizardForm.WelcomeLabel1.Width := WizardForm.ClientWidth - LeftOffset - ScaleX(15);
  WizardForm.WelcomeLabel2.Width := WizardForm.ClientWidth - LeftOffset - ScaleX(15);

  // Change Welcome page heading font size & remove bold
  WizardForm.WelcomeLabel1.Font.Size := 13;
  WizardForm.WelcomeLabel1.Font.Style :=
    WizardForm.WelcomeLabel1.Font.Style - [fsBold];
end;

procedure AdjustFinishedPage;
var
  LeftOffset: Integer;
begin
  WizardForm.WizardBitmapImage2.Width := ScaleX(165);

  // Calculate new left offset
  LeftOffset :=
    WizardForm.WizardBitmapImage2.Left +
    WizardForm.WizardBitmapImage2.Width +
    ScaleX(15);

  // Shift the Finished page components left
  WizardForm.FinishedHeadingLabel.Left := LeftOffset;
  WizardForm.FinishedLabel.Left := LeftOffset;
  WizardForm.RunList.Left := LeftOffset;

  // Shift the Finished page heading down
  WizardForm.FinishedHeadingLabel.Top := WizardForm.FinishedHeadingLabel.Top + ScaleY(2);

  // Expand the Finished page text width
  WizardForm.FinishedHeadingLabel.Width := WizardForm.ClientWidth - LeftOffset - ScaleX(15);
  WizardForm.FinishedLabel.Width := WizardForm.ClientWidth - LeftOffset - ScaleX(15);

  // Change Finished page heading font size & remove bold
  WizardForm.FinishedHeadingLabel.Font.Size := 13;
  WizardForm.FinishedHeadingLabel.Font.Style :=
    WizardForm.FinishedHeadingLabel.Font.Style - [fsBold];
end;

// Customize specific components at runtime
procedure InitializeWizard;
begin
  // Resize installer size
  WizardForm.Width := ScaleX(510);
  WizardForm.Height := ScaleY(395);
  
  // Change the Main Panel background & text color to match banner
  WizardForm.MainPanel.Color := $3A221D; // $BBGGRR format
  WizardForm.PageNameLabel.Font.Color := clWhite;
  WizardForm.PageDescriptionLabel.Font.Color := clWhite;

  AdjustWelcomePage;
  AdjustFinishedPage;
end;

// Interrupt page is a reskin of the Welcome page
procedure ShowInterruptedPage;
begin
  InterruptedPageActive := True;

  // Override Welcome page Labels
  WizardForm.WelcomeLabel1.Caption := '{#MyAppName} {#MyAppVersion} Setup Wizard was interrupted';
  WizardForm.WelcomeLabel2.Caption :=
    '{#MyAppName} {#MyAppVersion} setup was interrupted. ' +
    'Your system has not been modified. ' +
    'To install this program at a later time, please run the installation again. ' +
    'Click the Finish button to exit the Setup Wizard.';

  // Hide the buttons that go forward/backward
  WizardForm.BackButton.Visible := False;
  WizardForm.NextButton.Visible := False;

  // Turn the Cancel button into the "Finish" button
  WizardForm.CancelButton.Caption := 'Finish';

  // Move the Cancel button to the Next button's position
  WizardForm.CancelButton.Left := WizardForm.NextButton.Left;

  // Navigate to the Welcome page
  WizardForm.OuterNotebook.ActivePage := WizardForm.WelcomePage;
end;

procedure CancelButtonClick(CurPageID: Integer; var Cancel, Confirm: Boolean);
begin
  Confirm := False; // Suppress dialog

  // Immediately close the app if "Finished" (Cancel) is clicked
  // on the Interrupt page
  if InterruptedPageActive then begin
    Cancel := True; // Allow setup termination
    Exit;
  end;

  // Cancellation yes/no dialog
  if MsgBox(
    ExpandConstant('Are you sure you want to cancel {#MyAppName} {#MyAppVersion} installation?'),
    mbConfirmation,
    MB_YESNO
  ) = IDYES then begin
    if CurPageID = wpInstalling then begin
      Cancel := True; // During install, abort immediately after confirmation to allow for rollback
    end else begin
      ShowInterruptedPage;
      Cancel := False; // Stop the initial close so we can show the "Interrupted" screen
    end;
  end else begin
    Cancel := False; // User decided to stay
  end;
end;
