#!/usr/bin/env fish

function do_func
  argparse -n do_func 'h/help' 't/text=' -- $argv
  or return 1

  if set -lq _flag_help
    echo "spring-banner.fish -t/--text <TEXT_FOR_BANNER>"
    return
  end

  set -lq _flag_param
  or set -l _flag_text "Spring"

  curl "https://devops.datenkollektiv.de/renderBannerTxt?text={$_flag_text}&font=soft" > ../spring/src/main/resources/banner.txt
end

do_func $argv
